package service;

import org.example.model.Vote;
import org.example.repository.VoteRepository;

import java.util.List;

public class VoteService {
    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
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
}
