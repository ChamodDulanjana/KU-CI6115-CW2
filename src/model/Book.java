package model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private String isbn;
    private BookStatus status;

    public Book(String id, String title, String author, String category, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;  // Default status
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " by " + author +
                " (" + category + ", " + isbn + ") - " + status;
    }

}
