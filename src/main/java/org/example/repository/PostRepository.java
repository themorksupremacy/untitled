package org.example.repository;

import org.example.model.EndUser;
import org.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
