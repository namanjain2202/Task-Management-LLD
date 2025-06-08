package com.taskmanagement.repository;

import com.taskmanagement.model.Story;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StoryRepository implements Repository<Story> {
    private final Map<String, Story> stories = new ConcurrentHashMap<>();

    @Override
    public Story save(Story story) {
        stories.put(story.getId(), story);
        return story;
    }

    @Override
    public Optional<Story> findById(String id) {
        return Optional.ofNullable(stories.get(id));
    }

    @Override
    public List<Story> findAll() {
        return new ArrayList<>(stories.values());
    }

    @Override
    public void delete(String id) {
        stories.remove(id);
    }

    @Override
    public boolean exists(String id) {
        return stories.containsKey(id);
    }

    public List<Story> findByAssignedUser(String userId) {
        return stories.values().stream()
                .filter(story -> story.getAssignedUser() != null &&
                        story.getAssignedUser().getId().equals(userId))
                .toList();
    }
}
