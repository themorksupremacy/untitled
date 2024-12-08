package org.example.service;

import org.example.dto.CommentDTO;
import org.example.dto.VoteDTO;
import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Post;
import org.example.model.Vote;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.PostRepository;
import org.example.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private EndUserRepository endUserRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllComments() {
        // Arrange
        Comment comment1 = new Comment(1L, "Comment 1", new Timestamp(System.currentTimeMillis()), new Post(), new EndUser());
        Comment comment2 = new Comment(2L, "Comment 2", new Timestamp(System.currentTimeMillis()), new Post(), new EndUser());
        when(commentRepository.findAll()).thenReturn(List.of(comment1, comment2));

        // Act
        var comments = commentService.getAllComments();

        // Assert
        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void testSaveComment() {
        // Arrange
        List<VoteDTO> votes = new ArrayList<>();
        Post post = new Post(1L, "Post", new Timestamp(System.currentTimeMillis()), "Location", null);
        EndUser user = new EndUser(1L, "TestUser", "test@example.com", "1234");
        CommentDTO commentDTO = new CommentDTO(1L, "New Comment", new Timestamp(System.currentTimeMillis()), 1L, 1L, "TestUser", votes, 1, 1);
        Comment savedComment = new Comment(1L, "New Comment", new Timestamp(System.currentTimeMillis()), post, user);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(endUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // Act
        CommentDTO result = commentService.saveComment(commentDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Comment", result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testDeleteComment() {
        // Act
        commentService.deleteComment(1L);

        // Assert
        verify(commentRepository, times(1)).deleteById(1L);
    }
}

