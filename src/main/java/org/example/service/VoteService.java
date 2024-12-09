package org.example.service;

import org.example.dto.VoteDTO;
import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Vote;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final EndUserRepository endUserRepository;
    private final CommentRepository commentRepository;

    public VoteService(VoteRepository voteRepository, EndUserRepository endUserRepository, CommentRepository commentRepository) {
        this.voteRepository = voteRepository;
        this.endUserRepository = endUserRepository;
        this.commentRepository = commentRepository;
    }

    public List<VoteDTO> getAllVotes() {
        return voteRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Handle voting on comments (upvote or downvote) using boolean
    public VoteDTO castVote(Long commentId, Long userId, boolean voteType) {
        // Find the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID"));

        // Find the user by ID
        EndUser user = endUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        // Check if the user has already voted on this comment
        Optional<Vote> existingVote = voteRepository.findByCommentAndEndUser(comment, user);

        // If the user has already voted, update their vote
        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            if (vote.getType() == voteType) {
                // If the user is re-voting the same type, remove the vote
                voteRepository.delete(vote);
                return null;
            } else {
                // If the user is changing their vote, update it
                vote.setType(voteType);
                voteRepository.save(vote);
                return mapToDTO(vote);
            }
        }

        // Otherwise, create a new vote
        Vote newVote = new Vote();
        newVote.setType(voteType);
        newVote.setComment(comment);
        newVote.setEndUser(user);
        Vote savedVote = voteRepository.save(newVote);

        return mapToDTO(savedVote);
    }

    public VoteDTO mapToDTO(Vote vote) {
        return new VoteDTO(vote.getVoteId(), vote.getType(), vote.getEndUser().getId(), vote.getComment().getCommentId());
    }

    // In VoteService.java
    public void removeVote(Long commentId, Long userId) {
        // Find the comment by ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Comment ID"));

        // Find the user by ID
        EndUser user = endUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));

        // Check if the user has voted
        Optional<Vote> existingVote = voteRepository.findByCommentAndEndUser(comment, user);

        // If a vote exists, remove it
        existingVote.ifPresent(voteRepository::delete);
    }



}
