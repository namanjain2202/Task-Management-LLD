package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.model.TaskStatus;
import com.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    
    @Mock
    private UserService userService;

    private TaskService taskService;
    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, userService);
        
        testUser = new User("testUser", "password", "test@example.com");
        testTask = new Task("Test Task", "Test Description", LocalDateTime.now().plusDays(1), testUser);
        
        when(userService.getUser(anyString())).thenReturn(testUser);
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
    }

    @Test
    void createTask_ShouldCreateNewTask() {
        // Arrange
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        
        // Act
        Task createdTask = taskService.createTask("Test Task", "Test Description", deadline, testUser.getId());
        
        // Assert
        assertNotNull(createdTask);
        assertEquals("Test Task", createdTask.getTitle());
        assertEquals("Test Description", createdTask.getDescription());
        assertEquals(TaskStatus.PENDING, createdTask.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getUserTasks_ShouldReturnUserTasks() {
        // Arrange
        List<Task> expectedTasks = Arrays.asList(testTask);
        when(taskRepository.findByAssignedUser(testUser.getId())).thenReturn(expectedTasks);
        
        // Act
        List<Task> actualTasks = taskService.getUserTasks(testUser.getId());
        
        // Assert
        assertEquals(expectedTasks.size(), actualTasks.size());
        assertEquals(expectedTasks.get(0).getTitle(), actualTasks.get(0).getTitle());
        verify(taskRepository).findByAssignedUser(testUser.getId());
    }

    @Test
    void updateTaskStatus_ShouldUpdateStatus() {
        // Arrange
        when(taskRepository.findById(anyString())).thenReturn(Optional.of(testTask));
        
        // Act
        Task updatedTask = taskService.updateTaskStatus(testTask.getId(), TaskStatus.IN_PROGRESS);
        
        // Assert
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
        verify(taskRepository).save(testTask);
    }
}
