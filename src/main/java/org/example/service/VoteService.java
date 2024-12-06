package org.example.service;

import org.example.dto.CommentDTO;
import org.example.dto.VoteDTO;
import org.example.model.Comment;
import org.example.model.Vote;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Vote getVoteid(long id) {
        return voteRepository.getById(id);
    }

    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }

    public void deleteVote(Long id) {
        voteRepository.deleteById(id);
    }

    private VoteDTO mapToDTO(Vote vote){
        return new VoteDTO(vote.getId(), vote.getType(), vote.getUser().getId(), vote.getComment().getCommentId());
    }
}
