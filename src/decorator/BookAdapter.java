package decorator;

import model.Book;

public class BookAdapter implements BookComponent{
    private final Book book;

    public BookAdapter(Book book) {
        this.book = book;
    }

    @Override
    public String getId() {
        return book.getId();
    }

    @Override
    public String getTitle() {
        return book.getTitle();
    }

    @Override
    public String getDescription() {
        return book.toString();
    }

    @Override
    public Book getWrappedBook() {
        return book;
    }
}
