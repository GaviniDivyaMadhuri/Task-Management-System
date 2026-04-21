package com.taskflow.model;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String description;
    private String priority;   // HIGH, MEDIUM, LOW
    private String status;     // PENDING, IN_PROGRESS, COMPLETED
    private String category;
    private LocalDate dueDate;
    private LocalDate createdDate;

    public Task() {}

    public Task(String title, String description, String priority, String status, String category, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.category = category;
        this.dueDate = dueDate;
        this.createdDate = LocalDate.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public boolean isOverdue() {
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && !status.equals("COMPLETED");
    }

    @Override
    public String toString() { return title; }
}
