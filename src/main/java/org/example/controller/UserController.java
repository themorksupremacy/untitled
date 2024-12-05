package org.example.controller;

import org.example.model.EndUser;
import org.example.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<EndUser> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public EndUser createUser(@RequestBody EndUser endUser) {
        return userRepository.save(endUser);
    }
}
