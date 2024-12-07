package org.example.controller;

import org.example.dto.DashboardResponse;
import org.example.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final PostService postService;

    public DashboardController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboardData(@RequestBody String token) {
        try {
            // You can implement actual token verification logic here
            boolean isValid = token.equals("mockValidToken");  // You can replace this with real token validation
            if (!isValid) {
                return ResponseEntity.status(401).build();
            }

            // Fetch posts from the database
            List<String> posts = postService.getAllPosts().stream()
                    .map(post -> post.getContent()) // Assuming you want to display the content
                    .collect(Collectors.toList());

            // Create response with posts
            DashboardResponse response = new DashboardResponse("Welcome to the dashboard!", posts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new DashboardResponse("Error fetching data", null));
        }
    }
}
