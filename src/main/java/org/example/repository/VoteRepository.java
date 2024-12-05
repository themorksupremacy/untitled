package org.example.repository;

import org.example.model.EndUser;
import org.example.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
