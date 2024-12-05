package org.example.repository;

import org.example.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends  JpaRepository<Vote, Long>{
    Vote findVoteById(long id);
}

