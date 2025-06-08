package com.taskmanagement.service;

import com.taskmanagement.model.Story;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.StoryRepository;
import com.taskmanagement.exception.TaskManagementException;

import java.time.LocalDateTime;
import java.util.List;

public class StoryService {
    private final StoryRepository storyRepository;
    private final TaskService taskService;
    private final UserService userService;

    public StoryService(StoryRepository storyRepository, TaskService taskService, UserService userService) {
        this.storyRepository = storyRepository;
        this.taskService = taskService;
        this.userService = userService;
    }

    public Story createStory(String title, String description, LocalDateTime deadline, String assignedUserId) {
        User user = userService.getUser(assignedUserId);
        Story story = new Story(title, description, deadline, user);
        return storyRepository.save(story);
    }

    public Story getStory(String storyId) {
        return storyRepository.findById(storyId)
                .orElseThrow(() -> new TaskManagementException("Story not found"));
    }

    public Story addTaskToStory(String storyId, String taskId) {
        Story story = getStory(storyId);
        Task task = taskService.getTask(taskId);
        story.addTask(task);
        return storyRepository.save(story);
    }

    public List<Story> getUserStories(String userId) {
        return storyRepository.findByAssignedUser(userId);
    }

    public Story updateStory(String storyId, String title, String description, LocalDateTime deadline) {
        Story story = getStory(storyId);
        story.setTitle(title);
        story.setDescription(description);
        story.setDeadline(deadline);
        return storyRepository.save(story);
    }

    public void deleteStory(String storyId) {
        Story story = getStory(storyId);
        // Delete all tasks in the story
        story.getTasks().forEach(task -> taskService.deleteTask(task.getId()));
        storyRepository.delete(storyId);
    }
}
