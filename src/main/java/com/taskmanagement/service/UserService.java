package com.taskmanagement.service;

import com.taskmanagement.model.User;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.exception.TaskManagementException;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String password, String email) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new TaskManagementException("Username already exists");
        }
        User user = new User(username, password, email);
        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password));
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TaskManagementException("User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String userId) {
        if (!userRepository.exists(userId)) {
            throw new TaskManagementException("User not found");
        }
        userRepository.delete(userId);
    }
}
