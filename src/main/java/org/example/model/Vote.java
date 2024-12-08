package org.example.model;

import jakarta.persistence.*;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean type;

    @ManyToOne // Many posts can belong to one user
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private EndUser endUser;
    @ManyToOne
    @JoinColumn(name = "commentid", referencedColumnName = "id")
    private Comment comment;

    public Vote() {

    }

    public Vote(Long id, Boolean type, EndUser endUser, Comment comment) {
        super();
        this.id = id;
        this.type = type;
        this.endUser = endUser;
        this.comment = comment;
    }

    public Long getVoteId() {

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

    public EndUser getEndUser() {

        return endUser;
    }

    public void setEndUser(EndUser user) {

        this.endUser = user;
    }

    public Comment getComment() {

        return comment;
    }

    public void setComment(Comment comment) {

        this.comment = comment;
    }
}
