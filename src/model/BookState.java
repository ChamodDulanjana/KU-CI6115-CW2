package model;

public interface BookState {
    BookStatus getStatus();
    void borrow(Book book, String userId);
    void returnBook(Book book, String userId);
    int reserve(Book book, String userId);
}
