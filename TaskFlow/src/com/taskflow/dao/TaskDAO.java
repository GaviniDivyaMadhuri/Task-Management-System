package com.taskflow.dao;

import com.taskflow.model.Task;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskDAO {

    private static final String DATA_FILE = "tasks.dat";
    private List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    public TaskDAO() {
        loadFromFile();
        if (tasks.isEmpty()) seedSampleData();
    }

    public List<Task> getAllTasks() { return new ArrayList<>(tasks); }

    public List<Task> getByStatus(String status) {
        return tasks.stream().filter(t -> t.getStatus().equals(status)).collect(Collectors.toList());
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.add(task);
        saveToFile();
    }

    public void updateTask(Task updated) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updated.getId()) {
                tasks.set(i, updated);
                break;
            }
        }
        saveToFile();
    }

    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
        saveToFile();
    }

    public long countByStatus(String status) {
        return tasks.stream().filter(t -> t.getStatus().equals(status)).count();
    }

    public long countOverdue() {
        return tasks.stream().filter(Task::isOverdue).count();
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tasks);
            oos.writeInt(nextId);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            tasks = (List<Task>) ois.readObject();
            nextId = ois.readInt();
        } catch (Exception e) { tasks = new ArrayList<>(); }
    }

    private void seedSampleData() {
        addTask(new Task("Design Login & Registration UI",     "Wireframe and implement auth screens with validation",       "HIGH",   "COMPLETED",   "Frontend",   LocalDate.now().minusDays(2)));
        addTask(new Task("Setup PostgreSQL Database Schema",   "Define normalized tables: users, tasks, projects, logs",     "HIGH",   "COMPLETED",   "Backend",    LocalDate.now().minusDays(1)));
        addTask(new Task("Build REST API - Task CRUD",         "Implement GET/POST/PUT/DELETE endpoints with Spring Boot",   "HIGH",   "IN_PROGRESS", "Backend",    LocalDate.now().plusDays(3)));
        addTask(new Task("Write JUnit 5 Unit Tests",           "Cover all service and repository layer methods",             "MEDIUM", "IN_PROGRESS", "Testing",    LocalDate.now().plusDays(5)));
        addTask(new Task("Peer Code Review - Sprint 2",        "Review open PRs and leave constructive feedback",           "MEDIUM", "PENDING",     "Management", LocalDate.now().plusDays(1)));
        addTask(new Task("Fix NullPointerException in Auth",   "NPE thrown in UserService.getById() on missing user",       "HIGH",   "PENDING",     "Bug Fix",    LocalDate.now().minusDays(1)));
        addTask(new Task("Write API Documentation",            "Add Swagger/OpenAPI annotations to all endpoints",          "LOW",    "PENDING",     "Docs",       LocalDate.now().plusDays(7)));
        addTask(new Task("Deploy Build to Staging Server",     "Push Docker image and run smoke tests on staging env",      "MEDIUM", "PENDING",     "DevOps",     LocalDate.now().plusDays(4)));
    }
}
