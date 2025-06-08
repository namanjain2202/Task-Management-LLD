package com.taskmanagement;

import com.taskmanagement.controller.UserController;
import com.taskmanagement.controller.TaskController;
import com.taskmanagement.controller.StoryController;
import com.taskmanagement.model.Task;
import com.taskmanagement.model.Story;
import com.taskmanagement.model.User;
import com.taskmanagement.model.TaskStatus;
import com.taskmanagement.repository.*;
import com.taskmanagement.service.*;

import java.time.LocalDateTime;

public class TaskManagementApp {
    private final UserController userController;
    private final TaskController taskController;
    private final StoryController storyController;

    public TaskManagementApp() {
        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        TaskRepository taskRepository = new TaskRepository();
        StoryRepository storyRepository = new StoryRepository();

        // Initialize services
        UserService userService = new UserService(userRepository);
        TaskService taskService = new TaskService(taskRepository, userService);
        StoryService storyService = new StoryService(storyRepository, taskService, userService);

        // Initialize controllers
        this.userController = new UserController(userService);
        this.taskController = new TaskController(taskService, userController);
        this.storyController = new StoryController(storyService, userController);
    }

    public void demoWorkflow() {
        try {
            // Register users
            System.out.println("Registering users...");
            User john = userController.register("john", "password123", "john@example.com");
            User alice = userController.register("alice", "password456", "alice@example.com");

            // Login as John
            System.out.println("Logging in as John...");
            userController.login("john", "password123");

            // Create a story
            System.out.println("Creating a story...");
            Story story = storyController.createStory(
                "Website Redesign",
                "Redesign the company website",
                LocalDateTime.now().plusWeeks(2)
            );

            // Create tasks
            System.out.println("Creating tasks...");
            Task mainTask = taskController.createTask(
                "Homepage Design",
                "Design the new homepage layout",
                LocalDateTime.now().plusDays(5)
            );

            Task subtask1 = taskController.addSubtask(
                mainTask.getId(),
                "Header Component",
                "Design and implement the header component",
                LocalDateTime.now().plusDays(2)
            );

            Task subtask2 = taskController.addSubtask(
                mainTask.getId(),
                "Footer Component",
                "Design and implement the footer component",
                LocalDateTime.now().plusDays(2)
            );

            // Add tasks to story
            System.out.println("Adding tasks to story...");
            storyController.addTaskToStory(story.getId(), mainTask.getId());

            // Update task status
            System.out.println("Updating task status...");
            taskController.updateTaskStatus(subtask1.getId(), TaskStatus.IN_PROGRESS);

            // Check workload
            System.out.println("Checking workload...");
            System.out.println("John's workload: " + taskController.getMyWorkload());

            // Login as Alice
            System.out.println("Logging in as Alice...");
            userController.login("alice", "password456");

            // Create another task
            System.out.println("Creating more tasks...");
            Task aliceTask = taskController.createTask(
                "Content Migration",
                "Migrate content to the new website",
                LocalDateTime.now().plusDays(7)
            );

            // Add to story
            storyController.addTaskToStory(story.getId(), aliceTask.getId());

            System.out.println("Demo completed successfully!");

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskManagementApp app = new TaskManagementApp();
        app.demoWorkflow();
    }
}
