package com.taskflow.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String category;
    private LocalDate dueDate;
    private LocalDate createdDate;

    public Task(int id, String title, String description, String priority, String status, String category, LocalDate dueDate, LocalDate createdDate) {
        this.id = id; this.title = title; this.description = description;
        this.priority = priority; this.status = status; this.category = category;
        this.dueDate = dueDate; this.createdDate = createdDate;
    }

    public Task(String title, String description, String priority, String status, String category, LocalDate dueDate) {
        this(0, title, description, priority, status, category, dueDate, LocalDate.now());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    public String getPriority() { return priority; }
    public void setPriority(String p) { this.priority = p; }
    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }
    public String getCategory() { return category; }
    public void setCategory(String c) { this.category = c; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate d) { this.dueDate = d; }
    public LocalDate getCreatedDate() { return createdDate; }

    public boolean isOverdue() {
        return dueDate != null && dueDate.isBefore(LocalDate.now()) && !status.equals("COMPLETED");
    }
}
