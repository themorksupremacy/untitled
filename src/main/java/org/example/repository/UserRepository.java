package org.example.repository;

import org.example.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<EndUser, Long> {
}
