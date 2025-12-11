package model;

import java.time.LocalDate;

public class BorrowTransaction {
    private String id;
    private String userId;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;

    public BorrowTransaction(String id, String userId, String bookId, LocalDate borrowDate, LocalDate dueDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return returned; }

    public void markReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.returned = true;
    }

    @Override
    public String toString() {
        return String.format("TX[%s] user=%s book=%s borrow=%s due=%s returned=%s returnDate=%s",
                id, userId, bookId, borrowDate, dueDate, returned, returnDate);
    }
}
