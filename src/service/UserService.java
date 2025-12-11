package service;

import model.User;

import java.util.*;

public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public void registerUser(User user) {
        users.put(user.getId(), user);
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
}
