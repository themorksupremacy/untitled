package org.example.controller;

import org.example.dto.CommentDTO;
import org.example.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/comments")
    public List<CommentDTO> getAllComments(){
        return commentService.getAllComments();
    }
}
