

import org.example.dto.VoteDTO;
import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Vote;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.VoteRepository;
import org.example.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private EndUserRepository endUserRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllVotes() {
        // Arrange
        Vote vote1 = new Vote(1L, true, new EndUser(1L, "TestUser", "test@example.com", "1234"), new Comment());
        Vote vote2 = new Vote(2L, false, new EndUser(1L, "TestUser", "test@example.com", "1234"), new Comment());
        when(voteRepository.findAll()).thenReturn(List.of(vote1, vote2));

        // Act
        var votes = voteService.getAllVotes();

        // Assert
        assertEquals(2, votes.size());
        verify(voteRepository, times(1)).findAll();
    }

    @Test
    void testCastVote() {
        // Arrange
        Comment comment = new Comment(1L, "Comment", null, null, null);
        EndUser user = new EndUser(1L, "TestUser", "test@example.com", "1234");
        Vote newVote = new Vote(1L, true, user, comment);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(endUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(voteRepository.save(any(Vote.class))).thenReturn(newVote);

        // Act
        VoteDTO voteDTO = voteService.castVote(1L, 1L, true);

        // Assert
        assertNotNull(voteDTO);
        assertTrue(voteDTO.getType());
        verify(commentRepository, times(1)).findById(1L);
        verify(endUserRepository, times(1)).findById(1L);
    }

    @Test
    void testRemoveVote() {
        // Arrange
        Comment comment = new Comment(1L, "Comment", null, null, null);
        EndUser user = new EndUser(1L, "TestUser", "test@example.com", "1234");
        Vote vote = new Vote(1L, true, user, comment);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(endUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(voteRepository.findByCommentAndEndUser(comment, user)).thenReturn(Optional.of(vote));

        // Act
        voteService.removeVote(1L, 1L);

        // Assert
        verify(voteRepository, times(1)).delete(vote);
    }

    @Test
    void testMapToDTO() {
        // Arrange
        EndUser user = new EndUser(1L, "TestUser", "test@example.com", "password123");
        Comment comment = new Comment(1L, "Test Comment", null, null, null);
        Vote vote = new Vote(1L, true, user, comment);

        // Act
        VoteDTO voteDTO = voteService.mapToDTO(vote);

        // Assert
        assertEquals(1L, voteDTO.getId());
        assertEquals(1L, voteDTO.getUserId());
        assertEquals(1L, voteDTO.getComment());
    }
}
