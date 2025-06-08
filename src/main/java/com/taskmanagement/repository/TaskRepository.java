package com.taskmanagement.repository;

import com.taskmanagement.model.Task;
import com.taskmanagement.model.TaskStatus;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TaskRepository implements Repository<Task> {
    private final Map<String, Task> tasks = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void delete(String id) {
        tasks.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return tasks.containsKey(id);
    }

    public List<Task> findByAssignedUser(String userId) {
        return tasks.values().stream()
                .filter(task -> task.getAssignedUser() != null &&
                        task.getAssignedUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Task> findByStatus(TaskStatus status) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }
}
