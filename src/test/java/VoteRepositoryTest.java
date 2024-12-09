

import org.example.model.Comment;
import org.example.model.EndUser;
import org.example.model.Vote;
import org.example.repository.CommentRepository;
import org.example.repository.EndUserRepository;
import org.example.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DataJpaTest
class VoteRepositoryTest {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Test
    void testFindByCommentId() {
        // Arrange
        Comment comment = new Comment(1L, "Test Comment",null, null, null);
        commentRepository.save(comment);
        Vote vote = new Vote(null, true, null, comment);
        voteRepository.save(vote);

        // Act
        List<Vote> votes = voteRepository.findByCommentId(comment.getCommentId());

        // Assert
        assertEquals(1, votes.size());
        assertEquals(true, votes.get(0).getType());
    }

    @Test
    void testFindByCommentAndEndUser() {
        // Arrange
        EndUser user = new EndUser(null, "testuser", "test@example.com", "password123");
        Comment comment = new Comment(1L, "Test Comment", null, null, null);
        endUserRepository.save(user);
        commentRepository.save(comment);
        Vote vote = new Vote(null, true, user, comment);
        voteRepository.save(vote);

        // Act
        Optional<Vote> foundVote = voteRepository.findByCommentAndEndUser(comment, user);

        // Assert
        assertTrue(foundVote.isPresent());
        assertEquals(true, foundVote.get().getType());
    }
}
