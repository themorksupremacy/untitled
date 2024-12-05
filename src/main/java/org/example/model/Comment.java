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
    private Post post;
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private EndUser endUser;

    public Comment(){

    }

    public Comment(long commentId, String content, Timestamp timestamp, Post post, EndUser EndUser) {

        this.commentId = commentId;
        this.content = content;
        this.timestamp = timestamp;
        this.post = post;
        this.endUser = endUser;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public EndUser getEndUser() {
        return endUser;
    }

    public void setEndUser(EndUser endUser) {
        this.endUser = endUser;
    }


}
