package com.taskmanagement.controller;

import com.taskmanagement.model.User;
import com.taskmanagement.service.UserService;
import com.taskmanagement.exception.TaskManagementException;

import java.util.List;

public class UserController {
    private final UserService userService;
    private User currentUser;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User register(String username, String password, String email) {
        return userService.createUser(username, password, email);
    }

    public User login(String username, String password) {
        currentUser = userService.authenticateUser(username, password)
                .orElseThrow(() -> new TaskManagementException("Invalid credentials"));
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            throw new TaskManagementException("No user is logged in");
        }
        return currentUser;
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
