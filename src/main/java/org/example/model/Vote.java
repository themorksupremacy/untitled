package org.example.model;

import jakarta.persistence.*;

public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean type;

    @ManyToOne // Many posts can belong to one user
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private EndUser user;
    @JoinColumn(name = "commentid", referencedColumnName = "id")
    private Comment comment;

    public Vote() {

    }

    public Vote(Long id, Boolean type, EndUser user, Comment comment) {
        super();
        this.id = id;
        this.type = type;
        this.user = user;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public EndUser getUser() {
        return user;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
