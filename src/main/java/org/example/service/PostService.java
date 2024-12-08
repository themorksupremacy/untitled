package org.example.service;

import org.example.dto.PostDTO;
import org.example.model.EndUser;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.example.repository.EndUserRepository; // Add the EndUserRepository to fetch users
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final EndUserRepository endUserRepository;  // Add the EndUserRepository dependency

    // Constructor to inject dependencies
    public PostService(PostRepository postRepository, EndUserRepository endUserRepository) {
        this.postRepository = postRepository;
        this.endUserRepository = endUserRepository;
    }

    // Get all posts as DTOs, sorted by timestamp (most recent first)
    public List<PostDTO> getAllPosts() {
        // Fetch the posts sorted by timestamp in descending order
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Order.desc("timestamp")));

        // Convert the posts to DTOs
        return posts.stream()
                .map(this::convertToDTO)  // Convert Post entity to PostDTO
                .collect(Collectors.toList());
    }

    // Get a single post by ID as DTO
    public PostDTO getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::convertToDTO)  // Convert Post entity to PostDTO
                .orElse(null);  // Or throw an exception if preferred
    }

    // Save a post from DTO
    public PostDTO savePost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);  // Convert PostDTO to Post entity

        // Find the EndUser by ID and associate it with the Post entity
        EndUser user = endUserRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid User ID: " + postDTO.getUserId()));
        post.setEndUser(user);  // Set the EndUser for the Post

        Post savedPost = postRepository.save(post);  // Save the Post to the database
        return convertToDTO(savedPost);  // Return the saved Post as PostDTO
    }

    // Delete a post by ID
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Convert Post entity to PostDTO
    private PostDTO convertToDTO(Post post) {
        EndUser user = post.getEndUser();  // Get the user who created the post
        String username = user != null ? user.getUsername() : "";  // Get the username

        return new PostDTO(
                post.getId(),
                post.getContent(),
                post.getTimestamp(),
                post.getLocation(),
                username,  // Set the username in the PostDTO
                user != null ? user.getId() : null  // Pass userId to PostDTO
        );
    }

    // Convert PostDTO to Post entity
    private Post convertToEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setContent(postDTO.getContent());
        post.setTimestamp(postDTO.getTimestamp());
        post.setLocation(postDTO.getLocation());
        // No need to set EndUser here because it's handled in savePost
        return post;
    }
}
