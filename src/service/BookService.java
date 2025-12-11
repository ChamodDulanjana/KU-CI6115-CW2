package service;

import model.Book;
import model.BookStatus;

import java.util.*;

public class BookService {
    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, Queue<String>> reservations = new HashMap<>();  // reservation queue per bookId (FIFO)

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

    public boolean isAvailable(String id) {
        Book b = books.get(id);
        return b != null && b.getStatus() == BookStatus.AVAILABLE;
    }

    // --- Reservation helpers ---
   /* public void addReservation(String bookId, String userId) {
        reservations.computeIfAbsent(bookId, k -> new LinkedList<>()).add(userId);
    }

    public boolean hasReservations(String bookId) {
        Queue<String> q = reservations.get(bookId);
        return q != null && !q.isEmpty();
    }

    public Optional<String> pollNextReservation(String bookId) {
        Queue<String> q = reservations.get(bookId);
        if (q == null) return Optional.empty();
        String next = q.poll();
        return Optional.ofNullable(next);
    }

    public List<String> getReservationsForBook(String bookId) {
        Queue<String> q = reservations.get(bookId);
        if (q == null) return Collections.emptyList();
        return new ArrayList<>(q);
    }

    public Optional<String> peekNextReservation(String bookId) {
        Queue<String> q = reservations.get(bookId);
        if (q == null || q.isEmpty()) return Optional.empty();
        return Optional.ofNullable(q.peek());
    }*/
}
