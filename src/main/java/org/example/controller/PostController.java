package org.example.controller;

import org.example.dto.PostDTO;
import org.example.dto.CommentDTO;
import org.example.dto.VoteDTO;
import org.example.model.Vote;
import org.example.service.PostService;
import org.example.service.CommentService;
import org.example.service.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final VoteService voteService;

    public PostController(PostService postService, CommentService commentService, VoteService voteService) {
        this.postService = postService;
        this.commentService = commentService;
        this.voteService = voteService;
    }

    // Get all posts along with comments and votes
    @GetMapping("/posts")
    public List<PostDTO> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();

        // For each post, fetch its associated comments
        for (PostDTO post : posts) {
            List<CommentDTO> comments = commentService.getCommentsByPostId(post.getId());
            post.setComments(comments); // Ensure PostDTO has setComments method
        }

        return posts;
    }

    // Get a post by ID
    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable Long id) {
        PostDTO post = postService.getPostById(id);
        List<CommentDTO> comments = commentService.getCommentsByPostId(post.getId());
        post.setComments(comments); // Ensure PostDTO has setComments method
        return post;
    }

    // Create a new post
// Create a new post (Fixed URL and improved handling)
    @PostMapping("/posts")
    public ResponseEntity<?> savePost(@RequestBody PostDTO postDTO) {
        try {
            PostDTO savedPost = postService.savePost(postDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating post: " + e.getMessage());
        }
    }


    // Delete a post by ID
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    // Cast a vote on a comment
    @PostMapping("/comments/{commentId}/vote")
    public ResponseEntity<?> castVote(@PathVariable Long commentId, @RequestBody VoteDTO voteDTO) {
        try {
            Long userId = voteDTO.getUserId();
            Boolean type = voteDTO.getType(); // true for upvote, false for downvote, null to remove vote

            if (type == null) {
                // If type is null, remove the vote
                voteService.removeVote(commentId, userId);
                return ResponseEntity.ok("Vote removed successfully.");
            }

            // Cast or update the vote
            VoteDTO updatedVote = voteService.castVote(commentId, userId, type);
            return ResponseEntity.ok(updatedVote);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error casting vote: " + e.getMessage());
        }
    }


    // In your VoteController.java
    @DeleteMapping("/comments/{commentId}/removeVote")
    public ResponseEntity<Void> removeVote(@PathVariable Long commentId, @RequestParam Long userId) {
        voteService.removeVote(commentId, userId);
        return ResponseEntity.ok().build();
    }

    // Create a new comment
    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        try {
            // Save the comment using the service layer
            CommentDTO savedComment = commentService.saveComment(commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
