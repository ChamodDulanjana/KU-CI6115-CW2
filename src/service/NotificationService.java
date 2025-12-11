package service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationService {
    // Map userId - observer (User implements NotificationObserver)
    private final Map<String, NotificationObserver> observers = new ConcurrentHashMap<>();

    // subscribe (register) an observer for a userId
    public void subscribe(String userId, NotificationObserver observer) {
        if (userId == null || observer == null) return;
        observers.put(userId, observer);
    }

    // unsubscribe
    public void unsubscribe(String userId) {
        if (userId == null) return;
        observers.remove(userId);
    }

    // notify single user
    public void notifyUser(String userId, String message) {
        if (userId == null) return;
        NotificationObserver obs = observers.get(userId);
        if (obs != null) {
            obs.update(message);
        } else {
            // fallback: no observer registered — simple log
            System.out.println("[NotificationService] (no local observer) user=" + userId + " message=" + message);
        }
    }
}
