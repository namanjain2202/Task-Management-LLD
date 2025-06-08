package com.taskmanagement.service;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskStatus;
import com.taskmanagement.model.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.exception.TaskManagementException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task createTask(String title, String description, LocalDateTime deadline, String assignedUserId) {
        User user = userService.getUser(assignedUserId);
        Task task = new Task(title, description, deadline, user);
        return taskRepository.save(task);
    }

    public Task addSubtask(String parentTaskId, String title, String description, LocalDateTime deadline, String assignedUserId) {
        Task parentTask = getTask(parentTaskId);
        User user = userService.getUser(assignedUserId);
        
        Task subtask = new Task(title, description, deadline, user);
        parentTask.addSubtask(subtask);
        
        taskRepository.save(subtask);
        taskRepository.save(parentTask);
        
        return subtask;
    }

    public Task getTask(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskManagementException("Task not found"));
    }

    public List<Task> getUserTasks(String userId) {
        return taskRepository.findByAssignedUser(userId);
    }

    public Map<TaskStatus, Long> getUserWorkload(String userId) {
        return taskRepository.findByAssignedUser(userId).stream()
                .collect(Collectors.groupingBy(
                    Task::getStatus,
                    Collectors.counting()
                ));
    }

    public Task updateTaskStatus(String taskId, TaskStatus newStatus) {
        Task task = getTask(taskId);
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public Task updateTask(String taskId, String title, String description, LocalDateTime deadline) {
        Task task = getTask(taskId);
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(deadline);
        return taskRepository.save(task);
    }

    public void deleteTask(String taskId) {
        Task task = getTask(taskId);
        
        // If it's a subtask, remove it from parent
        if (task.getParentTask() != null) {
            task.getParentTask().getSubtasks().remove(task);
            taskRepository.save(task.getParentTask());
        }
        
        taskRepository.delete(taskId);
    }

    public Task moveTask(String taskId, String newParentId) {
        Task task = getTask(taskId);
        Task newParent = getTask(newParentId);
        
        // Remove from old parent if exists
        if (task.getParentTask() != null) {
            task.getParentTask().getSubtasks().remove(task);
            taskRepository.save(task.getParentTask());
        }
        
        // Add to new parent
        newParent.addSubtask(task);
        taskRepository.save(newParent);
        
        return taskRepository.save(task);
    }
}
