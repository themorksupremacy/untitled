

import org.example.dto.PostDTO;
import org.example.model.EndUser;
import org.example.model.Post;
import org.example.repository.EndUserRepository;
import org.example.repository.PostRepository;
import org.example.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private EndUserRepository endUserRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPosts() {
        // Arrange
        Post post1 = new Post(1L, "First Post", new Timestamp(System.currentTimeMillis()), "Location1", null);
        Post post2 = new Post(2L, "Second Post", new Timestamp(System.currentTimeMillis()), "Location2", null);

        when(postRepository.findAll(Sort.by(Sort.Order.desc("timestamp")))).thenReturn(Arrays.asList(post1, post2));

        // Act
        List<PostDTO> postDTOs = postService.getAllPosts();

        // Assert
        assertEquals(2, postDTOs.size());
        assertEquals("First Post", postDTOs.get(0).getContent());
        verify(postRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testGetPostById() {
        // Arrange
        Post post = new Post(1L, "Test Post", new Timestamp(System.currentTimeMillis()), "Location", null);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        // Act
        PostDTO postDTO = postService.getPostById(1L);

        // Assert
        assertNotNull(postDTO);
        assertEquals("Test Post", postDTO.getContent());
        assertEquals("Location", postDTO.getLocation());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPostByIdReturnsNullForInvalidId() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        PostDTO postDTO = postService.getPostById(999L);

        // Assert
        assertNull(postDTO);
        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    void testSavePost() {
        // Arrange
        EndUser user = new EndUser(1L, "TestUser", "test@example.com", "1234");
        PostDTO postDTO = new PostDTO(null, "New Post", new Timestamp(System.currentTimeMillis()), "Location", "TestUser", 1L);
        Post post = new Post(1L, "New Post", new Timestamp(System.currentTimeMillis()), "Location", user);
        Post savedPost = new Post(1L, "New Post", post.getTimestamp(), "Location", user);

        when(endUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        PostDTO savedPostDTO = postService.savePost(postDTO);

        // Assert
        assertNotNull(savedPostDTO);
        assertEquals("New Post", savedPostDTO.getContent());
        assertEquals(1L, savedPostDTO.getId());
        verify(endUserRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testSavePostThrowsExceptionWhenContentIsEmpty() {
        // Arrange
        PostDTO postDTO = new PostDTO(null, "  ", new Timestamp(System.currentTimeMillis()), "Location", "TestUser", 1L);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> postService.savePost(postDTO));
        assertEquals("Post content cannot be empty.", exception.getMessage());
    }

    @Test
    void testSavePostThrowsExceptionWhenUserIdIsInvalid() {
        // Arrange
        PostDTO postDTO = new PostDTO(null, "New Post", new Timestamp(System.currentTimeMillis()), "Location", "TestUser", 999L);

        when(endUserRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> postService.savePost(postDTO));
        assertEquals("Invalid User ID: 999", exception.getMessage());
    }

    @Test
    void testDeletePost() {
        // Act
        postService.deletePost(1L);

        // Assert
        verify(postRepository, times(1)).deleteById(1L);
    }
}
