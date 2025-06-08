package com.taskmanagement.controller;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskStatus;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.exception.TaskManagementException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TaskController {
    private final TaskService taskService;
    private final UserController userController;

    public TaskController(TaskService taskService, UserController userController) {
        this.taskService = taskService;
        this.userController = userController;
    }

    public Task createTask(String title, String description, LocalDateTime deadline) {
        String userId = userController.getCurrentUser().getId();
        return taskService.createTask(title, description, deadline, userId);
    }

    public Task addSubtask(String parentTaskId, String title, String description, LocalDateTime deadline) {
        String userId = userController.getCurrentUser().getId();
        return taskService.addSubtask(parentTaskId, title, description, deadline, userId);
    }

    public Task getTask(String taskId) {
        return taskService.getTask(taskId);
    }

    public List<Task> getMyTasks() {
        String userId = userController.getCurrentUser().getId();
        return taskService.getUserTasks(userId);
    }

    public Map<TaskStatus, Long> getMyWorkload() {
        String userId = userController.getCurrentUser().getId();
        return taskService.getUserWorkload(userId);
    }

    public Task updateTaskStatus(String taskId, TaskStatus newStatus) {
        return taskService.updateTaskStatus(taskId, newStatus);
    }

    public Task updateTask(String taskId, String title, String description, LocalDateTime deadline) {
        return taskService.updateTask(taskId, title, description, deadline);
    }

    public void deleteTask(String taskId) {
        taskService.deleteTask(taskId);
    }

    public Task moveTask(String taskId, String newParentId) {
        return taskService.moveTask(taskId, newParentId);
    }
}
