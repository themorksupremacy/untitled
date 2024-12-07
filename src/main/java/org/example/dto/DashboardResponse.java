package org.example.dto;

import java.util.List;

public class DashboardResponse {
    private String message;
    private List<String> posts;

    public DashboardResponse(String message, List<String> posts) {
        this.message = message;
        this.posts = posts;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }
}
