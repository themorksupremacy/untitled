package org.example.controller;

import org.example.model.EndUser;
import org.example.repository.EndUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EndUserController {

    private final EndUserRepository endUserRepository;

    public EndUserController(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    @GetMapping("/test-enduser")
    public String testEndUserRepository() {
        try {
            List<EndUser> users = endUserRepository.findAll();
            if (users.isEmpty()) {
                return "EndUser repository is connected, but no users found!";
            } else {
                // Build a response string with user details
                String userDetails = users.stream()
                        .map(user -> "User ID: " + user.getId() + "<br>" +
                                "Username: " + user.getUsername() + "<br>" +
                                "Email: " + user.getEmail() + "<br>" +
                                "Password Hash: " + user.getPasswordHash() + "<br>" +
                                "-----------------------------<br>")
                        .collect(Collectors.joining());

                return "EndUser repository is connected! Found " + users.size() + " users.<br><br>" + userDetails;
            }
        } catch (Exception e) {
            return "Error fetching users: " + e.getMessage();
        }
    }
}
