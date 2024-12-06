// src/main/java/org/example/service/PostService.java

package org.example.service;

import org.example.dto.PostDTO;
import org.example.model.Post;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Get all posts as DTOs
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a single post by ID as DTO
    public PostDTO getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null); // Or throw an exception if preferred
    }

    // Save a post from DTO
    public PostDTO savePost(PostDTO postDTO) {
        Post post = convertToEntity(postDTO);
        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    // Delete a post by ID
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    // Convert Post entity to PostDTO
    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getContent(),
                post.getTimestamp(),
                post.getLocation()
        );
    }

    // Convert PostDTO to Post entity
    private Post convertToEntity(PostDTO postDTO) {
        Post post = new Post();
        post.setId(postDTO.getId());
        post.setContent(postDTO.getContent());
        post.setTimestamp(postDTO.getTimestamp());
        post.setLocation(postDTO.getLocation());
        return post;
    }
}
