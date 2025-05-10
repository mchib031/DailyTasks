package com.example.dailytasks;

public class Task {
    private int id;
    private String description;
    private String status; // TO DO, IN PROGRESS, COMPLETED
    private int userId;

    public Task() {}

    public Task(String description, String status, int userId) {
        this.description = description;
        this.status = status;
    }

    public Task(int id, String description, String status, int userId) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
