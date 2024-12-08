package org.example.service;

import org.example.dto.CommentDTO;
import org.example.dto.VoteDTO;
import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Post;
import org.example.model.Vote;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.PostRepository;
import org.example.repository.VoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final EndUserRepository endUserRepository;
    private final VoteRepository voteRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, EndUserRepository endUserRepository, VoteRepository voteRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
        this.voteRepository = voteRepository;
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

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        // Fetch comments for the given post ID, sorted by timestamp (most recent first)
        return commentRepository.findByPostId(postId, Sort.by(Sort.Order.desc("timestamp")))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Delete a comment by its ID
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // Map Comment entity to DTO, now including the username and votes
    private CommentDTO mapToDTO(Comment comment) {
        EndUser user = comment.getEndUser();
        String username = (user != null) ? user.getUsername() : "Unknown User";
        List<VoteDTO> votes = voteRepository.findByCommentId(comment.getCommentId()).stream()
                .map(this::mapVoteToDTO)
                .collect(Collectors.toList());

        // Calculate upvote and downvote counts
        int upvoteCount = (int) votes.stream().filter(v -> Boolean.TRUE.equals(v.getType())).count();
        int downvoteCount = (int) votes.stream().filter(v -> Boolean.FALSE.equals(v.getType())).count();

        return new CommentDTO(
                comment.getCommentId(),
                comment.getContent(),
                comment.getTimestamp(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getEndUser() != null ? comment.getEndUser().getId() : null,
                username,
                votes,
                upvoteCount,
                downvoteCount
        );
    }

    // Map Vote entity to VoteDTO
    private VoteDTO mapVoteToDTO(Vote vote) {
        return new VoteDTO(
                vote.getVoteId(),
                vote.getType(),
                vote.getEndUser().getId(),
                vote.getComment().getCommentId()
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
