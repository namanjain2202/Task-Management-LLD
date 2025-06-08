# Task Management System

A Java-based task management system that allows users to manage tasks, subtasks, and stories within a project. The system follows clean architecture principles and implements the MVC pattern.

## Features

- User Management
  - Registration and login
  - User authentication
- Task Management
  - Create, read, update, and delete tasks
  - Add subtasks to existing tasks
  - Track task status (PENDING, IN_PROGRESS, COMPLETED, BLOCKED)
  - Set deadlines for tasks
- Story Management
  - Create and manage stories
  - Group related tasks under stories
  - Track story progress
- Workload Management
  - View user workload
  - Track tasks by status
- Hierarchy Management
  - Move tasks between different parents
  - Maintain task dependencies

## Architecture

The system follows the MVC (Model-View-Controller) pattern and clean architecture principles:

- Models: User, Task, Story
- Controllers: UserController, TaskController, StoryController
- Services: UserService, TaskService, StoryService
- Repositories: UserRepository, TaskRepository, StoryRepository

## Getting Started

1. Clone the repository
2. Open the project in your preferred Java IDE
3. Run `TaskManagementApp.java` to see a demo of the system

## Example Usage

```java
// Create a new instance of the application
TaskManagementApp app = new TaskManagementApp();

// Run the demo workflow
app.demoWorkflow();
```

## Design Patterns Used

- Singleton Pattern: For managing repositories
- Factory Pattern: For creating tasks and stories
- Strategy Pattern: For extensible task management
- Repository Pattern: For data access abstraction

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── taskmanagement/
│               ├── controller/
│               ├── model/
│               ├── repository/
│               ├── service/
│               └── exception/
``` 
