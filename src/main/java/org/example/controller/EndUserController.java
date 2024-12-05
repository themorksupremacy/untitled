package org.example.controller;

import org.example.model.EndUser;
import org.example.repository.EndUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EndUserController {

    private final EndUserRepository endUserRepository;

    public EndUserController(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    @GetMapping("/test-enduser")
    public String testEndUserRepository() {
        try {
            // Fetch all users to test the repository connection
            List<EndUser> users = endUserRepository.findAll();
            if (users.isEmpty()) {
                return "EndUser repository is connected, but no users found!";
            } else {
                return "EndUser repository is connected! Found " + users.size() + " users.";
            }
        } catch (Exception e) {
            return "EndUser repository connection failed: " + e.getMessage();
        }
    }
}
