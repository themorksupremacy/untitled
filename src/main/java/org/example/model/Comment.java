package org.example.model;

import jakarta.persistence.*;

import jakarta.persistence.GeneratedValue;

import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String content;
    private Timestamp timestamp;

    @ManyToOne // Many posts can belong to one user
    @JoinColumn(name = "postid", referencedColumnName = "id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private EndUser endUser;

    public Comment(){

    }

    public Comment(long commentId, String content, Timestamp timestamp, Post post, EndUser EndUser) {

        this.id = commentId;
        this.content = content;
        this.timestamp = timestamp;
        this.post = post;
        this.endUser = endUser;
    }

    //Getters & Setters

    public long getCommentId() {
        return id;
    }

    public void setCommentId(long commentId) {
        this.id = commentId;
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
