

import org.example.dto.EndUserDTO;
import org.example.model.EndUser;
import org.example.repository.EndUserRepository;
import org.example.service.EndUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EndUserServiceTest {

    @Mock
    private EndUserRepository endUserRepository;

    @InjectMocks
    private EndUserService endUserService;

    private EndUser user;

    @BeforeEach
    public void setUp() {
        user = new EndUser();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    public void testGetAllUsers() {
        EndUser user2 = new EndUser();
        user2.setId(2L);
        user2.setUsername("testuser2");
        user2.setEmail("test2@example.com");
        user2.setPassword("password456");

        when(endUserRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<EndUserDTO> users = endUserService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("testuser", users.get(0).getUsername());
        assertEquals("test2@example.com", users.get(1).getEmail());
        verify(endUserRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserByUsername() {
        when(endUserRepository.findByUsername("testuser")).thenReturn(user);

        EndUserDTO userDTO = endUserService.getUserByUsername("testuser");

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
        verify(endUserRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testSaveUser() {
        when(endUserRepository.save(user)).thenReturn(user);

        EndUser savedUser = endUserService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(endUserRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(endUserRepository).deleteById(1L);

        endUserService.deleteUser(1L);

        verify(endUserRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testAuthenticateSuccess() throws Exception {
        when(endUserRepository.findByUsername("testuser")).thenReturn(user);

        String token = endUserService.authenticate("testuser", "password123");

        assertNotNull(token);
        assertTrue(token.startsWith("token_"));
    }

    @Test
    public void testAuthenticateFailure() {
        when(endUserRepository.findByUsername("testuser")).thenReturn(user);

        Exception exception = assertThrows(Exception.class, () -> {
            endUserService.authenticate("testuser", "wrongpassword");
        });

        assertTrue(exception.getMessage().contains("Passwordhash: password123 entered password: wrongpassword"));
    }
}

