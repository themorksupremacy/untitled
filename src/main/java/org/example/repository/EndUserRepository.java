package org.example.repository;

import org.example.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndUserRepository extends JpaRepository<EndUser, Long> {
    EndUser findByUsername(String username);  // This is correct
}



