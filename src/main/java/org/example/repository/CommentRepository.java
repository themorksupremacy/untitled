package org.example.repository;

import org.example.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends  JpaRepository<Comment, Long>{
    Comment findCommentById(long id);
}

