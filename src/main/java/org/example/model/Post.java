package org.example.model;

import java.sql.Timestamp;

public class Post {
    private long postId;
    private String content;
    private Timestamp timestamp;
    private String location;
    private long userId;

    public Post(){}

    public Post(long postId, String content, Timestamp timestamp, String location, long userId) {
        this.postId = postId;
        this.content = content;
        this.timestamp = timestamp;
        this.location = location;
        this.userId = userId;
    }

    //Getters & Setters

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
