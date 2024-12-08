package org.example.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID
    private long id;
    private String content;
    private Timestamp timestamp;
    private String location;

    @ManyToOne // Many posts can belong to one user
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private EndUser endUser;

    public Post() {}

    // Constructor with all fields excluding ID since it's auto-generated
    public Post(String content, Timestamp timestamp, String location, EndUser endUser) {
        this.content = content;
        this.timestamp = timestamp;
        this.location = location;
        this.endUser = endUser;
    }

    // Getters & Setters
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public EndUser getEndUser() {
        return endUser;
    }

    public void setEndUser(EndUser endUser) {
        this.endUser = endUser;
    }
}
