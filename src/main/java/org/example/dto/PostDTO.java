package org.example.dto;

import java.sql.Timestamp;
import java.util.List;

public class PostDTO {
    private Long id; // This can be null for new posts
    private String content;
    private Timestamp timestamp;
    private String location;
    private List<CommentDTO> comments;
    private String username;
    private Long userId;

    // Default constructor
    public PostDTO() {}

    // Constructor for creating a new post without the ID
    public PostDTO(String content, Timestamp timestamp, String location, Long userId) {
        this.content = content;
        this.timestamp = timestamp;
        this.location = location;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
