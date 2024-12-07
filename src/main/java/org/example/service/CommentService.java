package org.example.service;

import org.example.dto.CommentDTO;
import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Post;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final EndUserRepository endUserRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, EndUserRepository endUserRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
    }

    // Get all comments as DTOs
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Save a new comment from a DTO
    public CommentDTO saveComment(CommentDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + commentDTO.getPostId()));
        EndUser user = endUserRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID: " + commentDTO.getUserId()));

        Comment comment = mapToEntity(commentDTO, post, user);
        Comment savedComment = commentRepository.save(comment);
        return mapToDTO(savedComment);
    }

    // Get comments by Post ID, with usernames included
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Delete a comment by its ID
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // Map Comment entity to DTO, now including the username
    private CommentDTO mapToDTO(Comment comment) {
        EndUser user = comment.getEndUser(); // Get associated EndUser to retrieve the username
        String username = (user != null) ? user.getUsername() : "Unknown User"; // Set username (fallback if user is null)

        return new CommentDTO(
                comment.getCommentId(),
                comment.getContent(),
                comment.getTimestamp(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getEndUser() != null ? comment.getEndUser().getId() : null,
                username // Add the username field to the DTO
        );
    }

    // Map DTO to Comment entity
    private Comment mapToEntity(CommentDTO commentDTO, Post post, EndUser user) {
        Comment comment = new Comment();
        comment.setCommentId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setTimestamp(commentDTO.getTimestamp());
        comment.setPost(post);
        comment.setEndUser(user);
        return comment;
    }
}
