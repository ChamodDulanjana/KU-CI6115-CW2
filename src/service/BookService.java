package service;

import model.Book;

import java.util.*;

public class BookService {
    private final Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        books.put(book.getId(), book);
        System.out.println("Book added: " + book);
    }

    public Optional<Book> findById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> listAll() {
        return new ArrayList<>(books.values());
    }

    public boolean removeBook(String id) {
        return books.remove(id) != null;
    }
}
