package org.example.dto;

import java.sql.Timestamp;
import java.util.List;

public class PostDTO {
    private Long id;
    private String content;
    private Timestamp timestamp;
    private String location;
    private List<CommentDTO> comments;
    private String username;  // Add username field
    private Long userId;  // Add userId field

    // Constructors
    public PostDTO() {}

    // Constructor with username and userId
    public PostDTO(Long id, String content, Timestamp timestamp, String location, String username, Long userId) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.location = location;
        this.username = username;  // Set username in constructor
        this.userId = userId;  // Set userId in constructor
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
        return username;  // Get username
    }

    public void setUsername(String username) {
        this.username = username;  // Set username
    }

    public Long getUserId() {
        return userId;  // Get userId
    }

    public void setUserId(Long userId) {
        this.userId = userId;  // Set userId
    }
}
