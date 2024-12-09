import org.example.Main;
import org.example.model.Comment;
import org.example.model.Post;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void testFindByPostId() {
        // Arrange
        Post post = new Post();
        post.setContent("Test Post"); // Set required fields
        post = postRepository.save(post); // Save the post to generate an ID

        Comment comment1 = new Comment();
        comment1.setContent("First Comment");
        comment1.setPost(post);

        Comment comment2 = new Comment();
        comment2.setContent("Second Comment");
        comment2.setPost(post);

        commentRepository.saveAll(List.of(comment1, comment2));

        // Act
        List<Comment> comments = commentRepository.findByPostId(post.getId());

        // Assert
        assertEquals(2, comments.size());
    }

    @Test
    void testFindByPostIdWithSort() {
        // Arrange
        Post post = new Post();
        post.setContent("Test Post"); // Set required fields
        post = postRepository.save(post); // Save the post to generate an ID

        Comment comment1 = new Comment();
        comment1.setContent("B Comment");
        comment1.setPost(post);

        Comment comment2 = new Comment();
        comment2.setContent("A Comment");
        comment2.setPost(post);

        commentRepository.saveAll(List.of(comment1, comment2));

        // Act
        List<Comment> comments = commentRepository.findByPostId(post.getId(), Sort.by("content"));

        // Assert
        assertEquals(2, comments.size());
        assertEquals("A Comment", comments.get(0).getContent());
    }
}
