package model;

public class BorrowedState implements BookState{
    @Override
    public BookStatus getStatus() {
        return BookStatus.BORROWED;
    }

    @Override
    public void borrow(Book book, String userId) {
        throw new IllegalStateException("Book is currently borrowed.");
    }

    @Override
    public void returnBook(Book book, String userId) {
        // When returned and queue exists => RESERVED, else AVAILABLE
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
