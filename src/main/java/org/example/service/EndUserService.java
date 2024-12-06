package org.example.service;

import org.example.dto.EndUserDTO;
import org.example.model.EndUser;
import org.example.repository.EndUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndUserService {
    private final EndUserRepository endUserRepository;

    public EndUserService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    public List<EndUserDTO> getAllUsers() {
        return endUserRepository.findAll()
                .stream()
                .map(endUser -> new EndUserDTO(
                        endUser.getId(),
                        endUser.getUsername(),
                        endUser.getEmail()))
                .collect(Collectors.toList());
    }

    public EndUserDTO getUserByUsername(String username) {
        EndUser endUser = endUserRepository.findByUsername(username);
        return new EndUserDTO(endUser.getId(), endUser.getUsername(), endUser.getEmail());
    }

    public EndUser saveUser(EndUser endUser) {
        return endUserRepository.save(endUser);
    }

    public void deleteUser(Long id) {
        endUserRepository.deleteById(id);
    }

    /**
     * Authenticates a user by validating the username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return an EndUserDTO if authentication is successful
     * @throws Exception if the username or password is invalid
     */
    public String authenticate(String username, String password) throws Exception {
        // Fetch user by username
        EndUser endUser = endUserRepository.findByUsername(username);

        // Check if the user exists and if the password matches
        if (endUser == null || !endUser.getPassword().equals(password)) {

            throw new Exception("Passwordhash: " + endUser.getPassword() + " entered password: " + password);
        }

        // Generate a token (for simplicity, returning a placeholder string)
        String token = generateToken(endUser);

        // Return the token if authentication is successful
        return token;
    }

    /**
     * Generates a token for the authenticated user (placeholder for actual JWT implementation).
     *
     * @param endUser the authenticated user
     * @return a generated token as a String
     */
    private String generateToken(EndUser endUser) {
        // Placeholder token generation logic (use JWT library in production)
        return "token_" + endUser.getId() + "_" + System.currentTimeMillis();
    }
}



