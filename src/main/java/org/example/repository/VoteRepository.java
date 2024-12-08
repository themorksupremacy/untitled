package org.example.repository;

import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    // Automatically generated query to find votes by comment ID
    List<Vote> findByCommentId(Long commentId);

    // Custom query to find a vote by a comment and an end user (userId)
    Optional<Vote> findByCommentAndEndUser(Comment comment, EndUser endUser);

}
