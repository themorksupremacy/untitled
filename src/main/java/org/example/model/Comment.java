package org.example.model;

import jakarta.persistence.*;

import jakarta.persistence.GeneratedValue;

import java.sql.Timestamp;

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    private String content;
    private Timestamp timestamp;

    @ManyToOne // Many posts can belong to one user
    @JoinColumn(name = "postid", referencedColumnName = "id")
    private long postId;
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private long userId;

    public Comment(){

    }

    public Comment(long commentId, String content, Timestamp timestamp, long postId, long userId) {

        this.commentId = commentId;
        this.content = content;
        this.timestamp = timestamp;
        this.postId = postId;
        this.userId = userId;
    }

    //Getters & Setters

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }


}
