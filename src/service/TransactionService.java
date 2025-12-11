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
    private final NotificationService notificationService;

    public TransactionService(BookService bookService, UserService userService, FineCalculator fineCalculator, NotificationService notificationService) {
        this.bookService = bookService;
        this.userService = userService;
        this.fineCalculator = fineCalculator;
        this.notificationService = notificationService;
    }

    // Borrow flow
    public BorrowTransaction borrowBook(String userId, String bookId) throws IllegalStateException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + userId));

        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found: " + bookId));

        // enforce user borrow limit
        long activeBorrows = transactions.values().stream()
                .filter(t -> !t.isReturned() && t.getUserId().equals(userId))
                .count();

        if (activeBorrows >= user.getBorrowLimit()) {
            throw new IllegalStateException("User has reached borrow limit.");
        }

        // DELEGATE BORROW RULES TO STATE OBJECT
        book.borrowRequested(userId);

        // create transaction
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(user.getBorrowDays());

        String txId = UUID.randomUUID().toString();
        BorrowTransaction tx = new BorrowTransaction(txId, userId, bookId, borrowDate, dueDate);
        transactions.put(txId, tx);

        // notify the borrower
        notificationService.notifyUser(userId,
                "You have borrowed book [" + book.getTitle() +
                        "] (ID=" + bookId + ") Due date: " + dueDate);

        System.out.println("Borrow successful. Due date: " + dueDate);
        return tx;
    }

    // Return flow
    public void returnBook(String userId, String bookId) throws IllegalStateException {
        Optional<BorrowTransaction> opt = transactions.values().stream()
                .filter(t -> !t.isReturned() && t.getUserId().equals(userId) && t.getBookId().equals(bookId))
                .findFirst();

        if (opt.isEmpty()) {
            throw new IllegalStateException("Active borrow transaction not found.");
        }

        BorrowTransaction tx = opt.get();

        LocalDate returnDate = LocalDate.now();
        tx.markReturned(returnDate);

        // calculate fine
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        long daysLate = ChronoUnit.DAYS.between(tx.getDueDate(), returnDate);
        if (daysLate < 0) daysLate = 0;

        double fine = fineCalculator.calculateFine(user, daysLate);

        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found"));

        // DELEGATE RETURN LOGIC TO STATE OBJECT
        Optional<String> nextReserver = book.returnRequested(userId);

        // notify next reserver (if any)
        nextReserver.ifPresent(nextId ->
                notificationService.notifyUser(nextId,
                        "The book [" + bookId + "] you reserved is now available for pickup.")
        );

        // notify the returner
        notificationService.notifyUser(userId,
                "Return processed for book " + bookId +
                        ". Days late: " + daysLate + " Fine: " + fine);
    }

    // Reserve flow
    public void reserveBook(String userId, String bookId) throws IllegalStateException {
        userService.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new IllegalStateException("Book not found"));

        // DELEGATE RESERVATION LOGIC TO STATE OBJECT
        int queuePosition = book.reserveRequested(userId);

        notificationService.notifyUser(userId,
                "You have been added to the reservation queue for book [" +
                        book.getTitle() + "] at position " + queuePosition);
    }

    // List transactions
    public List<BorrowTransaction> listAllTransactions() {
        return new ArrayList<>(transactions.values());
    }

    public List<BorrowTransaction> listActiveByUser(String userId) {
        return transactions.values().stream()
                .filter(t -> !t.isReturned() && t.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
