package decorator;

import model.Book;

public class BookDecorator implements BookComponent{
    protected final BookComponent inner;

    public BookDecorator(BookComponent inner) {
        this.inner = inner;
    }

    @Override
    public String getId() {
        return inner.getId();
    }

    @Override
    public String getTitle() {
        return inner.getTitle();
    }

    @Override
    public String getDescription() {
        return inner.getDescription();
    }

    @Override
    public Book getWrappedBook() {
        return inner.getWrappedBook();
    }
}
