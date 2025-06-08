package com.taskmanagement.repository;

import com.taskmanagement.model.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository implements Repository<User> {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(String id) {
        users.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return users.containsKey(id);
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
