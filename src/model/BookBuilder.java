package model;

import java.util.ArrayList;
import java.util.List;

public class BookBuilder {
    private String id;
    private String title;
    private String author;
    private String category;
    private String isbn;

    // optional
    private String edition;
    private String summary;
    private List<String> tags = new ArrayList<>();

    public BookBuilder(String id, String title, String author, String category, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
    }

    public BookBuilder edition(String edition) {
        this.edition = edition;
        return this;
    }

    public BookBuilder summary(String summary) {
        this.summary = summary;
        return this;
    }

    public BookBuilder addTag(String tag) {
        if (tag != null && !tag.isBlank()) this.tags.add(tag);
        return this;
    }

    public Book build() {
        return new Book(id, title, author, category, isbn);
    }
}
