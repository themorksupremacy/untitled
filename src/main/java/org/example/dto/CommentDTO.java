// src/main/java/org/example/dto/CommentDTO.java

package org.example.dto;

import java.sql.Timestamp;

public class CommentDTO {
    private long id;
    private String content;
    private Timestamp timestamp;
    private Long postId; // Reference to the associated Post entity by ID
    private Long userId; // Reference to the associated EndUser entity by ID
    private String username; // Added field for the username

    public CommentDTO() {}

    public CommentDTO(long id, String content, Timestamp timestamp, Long postId, Long userId, String username) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
