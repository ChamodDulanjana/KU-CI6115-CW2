package service;

public interface NotificationObserver {
    /**
     * Called when a notification is published for this observer.
     * Implementations decide how to present/store the message.
     */
    void update(String message);
}
