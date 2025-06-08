package com.taskmanagement.controller;

import com.taskmanagement.model.Story;
import com.taskmanagement.service.StoryService;
import com.taskmanagement.exception.TaskManagementException;

import java.time.LocalDateTime;
import java.util.List;

public class StoryController {
    private final StoryService storyService;
    private final UserController userController;

    public StoryController(StoryService storyService, UserController userController) {
        this.storyService = storyService;
        this.userController = userController;
    }

    public Story createStory(String title, String description, LocalDateTime deadline) {
        String userId = userController.getCurrentUser().getId();
        return storyService.createStory(title, description, deadline, userId);
    }

    public Story getStory(String storyId) {
        return storyService.getStory(storyId);
    }

    public Story addTaskToStory(String storyId, String taskId) {
        return storyService.addTaskToStory(storyId, taskId);
    }

    public List<Story> getMyStories() {
        String userId = userController.getCurrentUser().getId();
        return storyService.getUserStories(userId);
    }

    public Story updateStory(String storyId, String title, String description, LocalDateTime deadline) {
        return storyService.updateStory(storyId, title, description, deadline);
    }

    public void deleteStory(String storyId) {
        storyService.deleteStory(storyId);
    }
}
