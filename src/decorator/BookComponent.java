package decorator;

import model.Book;

public interface BookComponent {
    String getId();
    String getTitle();
    String getDescription(); // full description for UI
    Book getWrappedBook();   // access original Book if needed
}
