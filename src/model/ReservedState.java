package model;

import java.util.Optional;

public class ReservedState implements BookState{
    @Override
    public BookStatus getStatus() {
        return BookStatus.RESERVED;
    }

    @Override
    public void borrow(Book book, String userId) {
        // Only the first reserver can borrow
        Optional<String> firstOpt = book.peekNextReserver();
        if (firstOpt.isEmpty() || !firstOpt.get().equals(userId)) {
            throw new IllegalStateException("Book reserved for another user. You are not allowed to borrow it now.");
        }
        // allowed — consume reservation and move to BORROWED
        book.pollNextReserver();
        book.setState(new BorrowedState());
    }

    @Override
    public void returnBook(Book book, String userId) {
        // If somehow returned while reserved (edge case) move to RESERVED (keep queue)
        if (book.hasReservations()) {
            book.setState(new ReservedState());
        } else {
            book.setState(new AvailableState());
        }
    }

    @Override
    public int reserve(Book book, String userId) {
        return book.addReservation(userId);
    }
}
