package com.taskmanagement.controller;

import com.taskmanagement.model.User;
import com.taskmanagement.service.UserService;
import com.taskmanagement.exception.TaskManagementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    private UserController userController;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        testUser = new User("testUser", "password", "test@example.com");
    }

    @Test
    void register_ShouldCreateNewUser() {
        // Arrange
        when(userService.createUser(anyString(), anyString(), anyString())).thenReturn(testUser);

        // Act
        User resultUser = userController.register("testUser", "password", "test@example.com");

        // Assert
        assertNotNull(resultUser);
        assertEquals(testUser, resultUser);
        verify(userService).createUser("testUser", "password", "test@example.com");
    }

    @Test
    void login_WithValidCredentials_ShouldReturnUser() {
        // Arrange
        when(userService.authenticateUser("testUser", "password")).thenReturn(Optional.of(testUser));

        // Act
        User loggedInUser = userController.login("testUser", "password");

        // Assert
        assertNotNull(loggedInUser);
        assertEquals(testUser, loggedInUser);
        assertEquals(loggedInUser, userController.getCurrentUser());
        verify(userService).authenticateUser("testUser", "password");
    }

    @Test
    void login_WithInvalidCredentials_ShouldThrowException() {
        // Arrange
        when(userService.authenticateUser("testUser", "wrongPassword")).thenReturn(Optional.empty());

        // Act & Assert
        TaskManagementException exception = assertThrows(TaskManagementException.class,
            () -> userController.login("testUser", "wrongPassword"));
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userService).authenticateUser("testUser", "wrongPassword");
    }

    @Test
    void logout_ShouldClearCurrentUser() {
        // Arrange
        when(userService.authenticateUser("testUser", "password")).thenReturn(Optional.of(testUser));
        userController.login("testUser", "password");
        assertNotNull(userController.getCurrentUser());

        // Act
        userController.logout();

        // Assert
        TaskManagementException exception = assertThrows(TaskManagementException.class,
            () -> userController.getCurrentUser());
        assertEquals("No user is logged in", exception.getMessage());
    }

    @Test
    void getCurrentUser_WhenNoUserLoggedIn_ShouldThrowException() {
        // Act & Assert
        TaskManagementException exception = assertThrows(TaskManagementException.class,
            () -> userController.getCurrentUser());
        assertEquals("No user is logged in", exception.getMessage());
    }

    @Test
    void getAllUsers_ShouldReturnUserList() {
        // Arrange
        List<User> expectedUsers = Arrays.asList(
            testUser,
            new User("user2", "password2", "user2@example.com")
        );
        when(userService.getAllUsers()).thenReturn(expectedUsers);

        // Act
        List<User> users = userController.getAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(expectedUsers.size(), users.size());
        assertEquals(expectedUsers, users);
        verify(userService).getAllUsers();
    }
}
