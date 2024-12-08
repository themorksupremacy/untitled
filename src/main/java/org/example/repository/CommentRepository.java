package org.example.repository;

import org.example.model.Comment;
import org.example.model.EndUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByPostId(Long postId, Sort sort);
}
