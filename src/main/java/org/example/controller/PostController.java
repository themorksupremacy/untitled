package org.example.controller;

import org.example.dto.PostDTO;
import org.example.dto.CommentDTO;
import org.example.service.PostService;
import org.example.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    // Get all posts along with comments
    @GetMapping("/posts")
    public List<PostDTO> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();

        // For each post, fetch its associated comments
        for (PostDTO post : posts) {
            List<CommentDTO> comments = commentService.getCommentsByPostId(post.getId());
            post.setComments(comments);  // Ensure PostDTO has setComments method
        }

        return posts;
    }

    // Get a post by ID
    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable Long id) {
        PostDTO post = postService.getPostById(id);
        List<CommentDTO> comments = commentService.getCommentsByPostId(post.getId());
        post.setComments(comments);  // Ensure PostDTO has setComments method
        return post;
    }

    // Create a new post
    @PostMapping
    public PostDTO savePost(@RequestBody PostDTO postDTO) {
        return postService.savePost(postDTO);
    }

    // Delete a post by ID
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
