// src/main/java/org/example/dto/PostDTO.java

package org.example.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PostDTO {
    private Long id;
    private String content;
    private Timestamp timestamp;
    private String location;

    // Constructors
    public PostDTO() {}

    public PostDTO(Long id, String content, Timestamp timestamp, String location) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.location = location;
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
}
