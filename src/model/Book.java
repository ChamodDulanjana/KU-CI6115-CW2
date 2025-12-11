package model;

import java.util.*;

public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private String isbn;

    // State pattern
    private BookState state;

    // Per-book FIFO reservation queue (userIds)
    private final Queue<String> reservations = new LinkedList<>();

    public Book(String id, String title, String author, String category, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isbn = isbn;
        this.state = new AvailableState();
    }

    // --- State API (used by services) ---

    /**
     * Attempt to borrow the book. Will throw IllegalStateException if not allowed.
     * If allowed, may consume reservation and transition state to BORROWED.
     */
    public void borrowRequested(String userId) {
        state.borrow(this, userId);
    }

    /**
     * Attempt to return the book. Transitions state to RESERVED (peek next) or AVAILABLE.
     * Returns Optional of the next reserver's userId (peeked) to be notified (but not removed).
     */
    public Optional<String> returnRequested(String userId) {
        state.returnBook(this, userId);
        return peekNextReserver();
    }

    /**
     * Request a reservation for userId. Returns queue position (1-based).
     */
    public int reserveRequested(String userId) {
        return state.reserve(this, userId);
    }

    // --- internal reservation helpers used by states ---

    // Add a reservation to queue. Returns position (1-based).
    int addReservation(String userId) {
        // prevent duplicate reservations by same user
        if (reservations.contains(userId)) {
            // return existing position
            int i = 1;
            for (String u : reservations) {
                if (u.equals(userId)) return i;
                i++;
            }
        }
        reservations.add(userId);
        return reservations.size();
    }

    Optional<String> peekNextReserver() {
        return Optional.ofNullable(reservations.peek());
    }

    /**
     * Remove and return the head of the reservation queue.
     * Use only when the reserver actually borrows.
     */
    Optional<String> pollNextReserver() {
        return Optional.ofNullable(reservations.poll());
    }

    public boolean hasReservations() {
        return !reservations.isEmpty();
    }

    // --- state accessor/mutator used by state classes ---
    public void setState(BookState newState) {
        this.state = newState;
    }

    public BookStatus getStatus() {
        return state.getStatus();
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getIsbn() { return isbn; }

    @Override
    public String toString() {
        return "[" + id + "] " + title + " by " + author + " (" + category + ", " + isbn + ") - " + getStatus();
    }
}
