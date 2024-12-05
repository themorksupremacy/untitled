package org.example.repository;

import org.example.model.Comment;
import org.example.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
