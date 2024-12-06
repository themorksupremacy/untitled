package org.example.dto;

import jakarta.persistence.*;
import org.example.model.Comment;
import org.example.model.EndUser;

public class VoteDTO {
    private Long id;
    private Boolean type;
    private long userId;
    private long commentId;

    public VoteDTO(){}

    public VoteDTO(Long id, Boolean type, long userId, long commentId) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.commentId = commentId;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getComment() {
        return commentId;
    }

    public void setComment(long commentId) {
        this.commentId = commentId;
    }
}
