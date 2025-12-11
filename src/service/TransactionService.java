package service;

import model.Book;
import model.BookStatus;
import model.BorrowTransaction;
import model.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private final Map<String, BorrowTransaction> transactions = new HashMap<>();
    private final BookService bookService;
    private final UserService userService;
    private final FineCalculator fineCalculator;

    public TransactionService(BookService bookService, UserService userService, FineCalculator fineCalculator) {
        this.bookService = bookService;
        this.userService = userService;
        this.fineCalculator = fineCalculator;
    }

    // Borrow flow
    public BorrowTransaction borrowBook(String userId, String bookId) throws IllegalStateException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));

        BookStatus status = book.getStatus();

        // If book is BORROWED -> cannot borrow
        if (status == BookStatus.BORROWED) {
            throw new IllegalStateException("Book not available for borrowing.");
        }

        // If there are reservations for the book, find who is first in queue (if any)
        List<String> currentQueue = bookService.getReservationsForBook(bookId);
        String firstInQueue = currentQueue.isEmpty() ? null : currentQueue.getFirst();

        // If book is AVAILABLE
        if (status == BookStatus.AVAILABLE) {
            if (firstInQueue != null) {
                // There is a reservation queue: only the first reserver can take it
                if (!firstInQueue.equals(userId)) {
                    throw new IllegalStateException("Book reserved for another user. You are not allowed to borrow it now.");
                }
                // If firstInQueue == userId, we allow borrow and remove them from queue
                bookService.pollNextReservation(bookId); // consume reservation
            }
            // no queue -> free to borrow
        } else if (status == BookStatus.RESERVED) {
            // Book status is RESERVED — only allow if the user is the first in reservation queue
            if (firstInQueue == null || !firstInQueue.equals(userId)) {
                throw new IllegalStateException("Book reserved for another user. You are not allowed to borrow it now.");
            }
            // allowed — remove from reservation queue
            bookService.pollNextReservation(bookId);
        }

        // check user borrow limit (count active borrows)
        long activeBorrows = transactions.values().stream()
                .filter(t -> !t.isReturned() && t.getUserId().equals(userId))
                .count();

        if (activeBorrows >= user.getBorrowLimit()) {
            throw new IllegalStateException("User has reached borrow limit.");
        }

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(user.getBorrowDays());

        String txId = UUID.randomUUID().toString();
        BorrowTransaction tx = new BorrowTransaction(txId, userId, bookId, borrowDate, dueDate);
        transactions.put(txId, tx);

        // update book status to BORROWED
        bookService.setStatus(bookId, BookStatus.BORROWED);

        System.out.println("Borrow successful. Due date: " + dueDate);
        return tx;
    }

    // Return flow
    public void returnBook(String userId, String bookId) throws IllegalStateException {
        Optional<BorrowTransaction> opt = transactions.values().stream()
                .filter(t -> !t.isReturned() && t.getUserId().equals(userId) && t.getBookId().equals(bookId))
                .findFirst();

        if (opt.isEmpty()) {
            throw new IllegalStateException("Active borrow transaction not found for this user/book.");
        }

        BorrowTransaction tx = opt.get();
        LocalDate returnDate = LocalDate.now();
        tx.markReturned(returnDate);

        long daysLate = ChronoUnit.DAYS.between(tx.getDueDate(), returnDate);
        if (daysLate < 0) daysLate = 0;

        // get user to compute fine using strategy
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));

        double fine = fineCalculator.calculateFine(user, daysLate);

        // reservation handling unchanged...
        if (bookService.hasReservations(bookId)) {
            Optional<String> nextUserId = bookService.pollNextReservation(bookId);
            if (nextUserId.isPresent()) {
                bookService.setStatus(bookId, BookStatus.RESERVED);
                System.out.println("Book returned. Reserved for user: " + nextUserId.get());
                System.out.println("Notify user (" + nextUserId.get() + "): the book [" + bookId + "] is available for pickup.");
            } else {
                bookService.setStatus(bookId, BookStatus.AVAILABLE);
            }
        } else {
            bookService.setStatus(bookId, BookStatus.AVAILABLE);
        }

        System.out.println("Return processed. Days late: " + daysLate + " Fine: " + fine);
    }

    // Reserve flow
    public void reserveBook(String userId, String bookId) throws IllegalStateException {
        userService.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));

        if (book.getStatus() == BookStatus.AVAILABLE) {
            // If available, just mark reserved and add to queue (user can pick immediately or we can let them borrow)
            bookService.addReservation(bookId, userId);
            bookService.setStatus(bookId, BookStatus.RESERVED);
            System.out.println("Book is currently available — reserved for you. Please pick up or borrow it soon.");
            return;
        }

        // If borrowed or reserved, add to reservation queue
        bookService.addReservation(bookId, userId);
        System.out.println("Reservation added. You are in queue position: " +
                (bookService.getReservationsForBook(bookId).size()));
    }

    // List transactions
    public List<BorrowTransaction> listAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    // helper: list active borrows for a user
    public List<BorrowTransaction> listActiveByUser(String userId) {
        return transactions.values().stream()
                .filter(t -> !t.isReturned() && t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
