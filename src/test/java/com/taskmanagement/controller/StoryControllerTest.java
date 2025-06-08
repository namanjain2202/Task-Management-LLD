package com.taskmanagement.controller;

import com.taskmanagement.model.Story;
import com.taskmanagement.model.User;
import com.taskmanagement.service.StoryService;
import com.taskmanagement.exception.TaskManagementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StoryControllerTest {
    @Mock
    private StoryService storyService;

    @Mock
    private UserController userController;

    private StoryController storyController;
    private User testUser;
    private Story testStory;
    private LocalDateTime deadline;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storyController = new StoryController(storyService, userController);
        testUser = new User("testUser", "password", "test@example.com");
        deadline = LocalDateTime.now().plusDays(7);
        testStory = new Story("Test Story", "Test Description", deadline, testUser.getId());

        when(userController.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    void createStory_ShouldCreateNewStory() {
        // Arrange
        when(storyService.createStory(anyString(), anyString(), any(LocalDateTime.class), anyString()))
            .thenReturn(testStory);

        // Act
        Story result = storyController.createStory("Test Story", "Test Description", deadline);

        // Assert
        assertNotNull(result);
        assertEquals(testStory, result);
        verify(storyService).createStory("Test Story", "Test Description", deadline, testUser.getId());
        verify(userController).getCurrentUser();
    }

    @Test
    void getStory_ShouldReturnStory() {
        // Arrange
        when(storyService.getStory(testStory.getId())).thenReturn(testStory);

        // Act
        Story result = storyController.getStory(testStory.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testStory, result);
        verify(storyService).getStory(testStory.getId());
    }

    @Test
    void addTaskToStory_ShouldAddTaskAndReturnUpdatedStory() {
        // Arrange
        String taskId = "task123";
        when(storyService.addTaskToStory(testStory.getId(), taskId)).thenReturn(testStory);

        // Act
        Story result = storyController.addTaskToStory(testStory.getId(), taskId);

        // Assert
        assertNotNull(result);
        assertEquals(testStory, result);
        verify(storyService).addTaskToStory(testStory.getId(), taskId);
    }

    @Test
    void getMyStories_ShouldReturnUserStories() {
        // Arrange
        List<Story> expectedStories = Arrays.asList(
            testStory,
            new Story("Story 2", "Description 2", deadline, testUser.getId())
        );
        when(storyService.getUserStories(testUser.getId())).thenReturn(expectedStories);

        // Act
        List<Story> results = storyController.getMyStories();

        // Assert
        assertNotNull(results);
        assertEquals(expectedStories.size(), results.size());
        assertEquals(expectedStories, results);
        verify(storyService).getUserStories(testUser.getId());
        verify(userController).getCurrentUser();
    }

    @Test
    void updateStory_ShouldUpdateAndReturnStory() {
        // Arrange
        LocalDateTime newDeadline = deadline.plusDays(1);
        Story updatedStory = new Story("Updated Title", "Updated Description", newDeadline, testUser.getId());
        when(storyService.updateStory(testStory.getId(), "Updated Title", "Updated Description", newDeadline))
            .thenReturn(updatedStory);

        // Act
        Story result = storyController.updateStory(testStory.getId(), "Updated Title", "Updated Description", newDeadline);

        // Assert
        assertNotNull(result);
        assertEquals(updatedStory, result);
        verify(storyService).updateStory(testStory.getId(), "Updated Title", "Updated Description", newDeadline);
    }

    @Test
    void deleteStory_ShouldDeleteStory() {
        // Act
        storyController.deleteStory(testStory.getId());

        // Assert
        verify(storyService).deleteStory(testStory.getId());
    }

    @Test
    void methods_WhenNotLoggedIn_ShouldThrowException() {
        // Arrange
        when(userController.getCurrentUser()).thenThrow(new TaskManagementException("No user is logged in"));

        // Act & Assert
        assertThrows(TaskManagementException.class, () -> storyController.createStory("Test", "Test", deadline));
        assertThrows(TaskManagementException.class, () -> storyController.getMyStories());
    }
}
