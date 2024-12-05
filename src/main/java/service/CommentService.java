package service;

import org.example.model.Comment;
import org.example.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {

        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllComments() {

        return commentRepository.findAll();
    }

    public Comment getCommentById(long id) {

        return commentRepository.findCommentById(id);
    }

    public Comment saveComment(Comment comment) {

        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {

        commentRepository.deleteById(id);
    }
}