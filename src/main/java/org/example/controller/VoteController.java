package org.example.controller;

import org.example.dto.VoteDTO;
import org.example.model.Vote;
import org.example.service.VoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoteController {
    private final VoteService voteService;

    public VoteController(VoteService voteService){
        this.voteService = voteService;
    }

    @GetMapping("/votes")
    public List<VoteDTO> getAllVotes(){
        return voteService.getAllVotes();
    }
}

