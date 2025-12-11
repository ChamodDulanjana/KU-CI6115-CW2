package service;

import model.User;

import java.util.*;

public class UserService {
    private final NotificationService notificationService;

    private final Map<String, User> users = new HashMap<>();

    public UserService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void registerUser(User user) {
        users.put(user.getId(), user);
        // subscribe user to notification service
        notificationService.subscribe(user.getId(), user);

        System.out.println("User registered: " + user);
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> listAll() {
        return new ArrayList<>(users.values());
    }

    public boolean exists(String id) {
        return users.containsKey(id);
    }

    public void removeUser(String id) {
        users.remove(id);
        notificationService.unsubscribe(id);
    }
}
