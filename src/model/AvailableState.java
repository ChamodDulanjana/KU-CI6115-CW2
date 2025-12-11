package model;

public class AvailableState implements BookState{
    @Override
    public BookStatus getStatus() {
        return BookStatus.AVAILABLE;
    }

    @Override
    public void borrow(Book book, String userId) {
        // If there is a reservation queue, only the first reserver can borrow
        String first = book.peekNextReserver().orElse(null);
        if (first != null && !first.equals(userId)) {
            throw new IllegalStateException("Book reserved for another user. You are not allowed to borrow it now.");
        }
        // If first == userId, consume it. If no queue, proceed.
        if (first != null && first.equals(userId)) {
            book.pollNextReserver(); // remove the reservation entry
        }
        book.setState(new BorrowedState());
    }

    @Override
    public void returnBook(Book book, String userId) {
        // shouldn't happen: returning when available — but just ensure state remains AVAILABLE
        book.setState(new AvailableState());
    }

    @Override
    public int reserve(Book book, String userId) {
        int pos = book.addReservation(userId);
        // Once a reservation exists, change the state to RESERVED
        book.setState(new ReservedState());
        return pos;
    }
}
