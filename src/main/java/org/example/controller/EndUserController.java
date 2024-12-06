package org.example.controller;

import org.example.dto.EndUserDTO;
import org.example.service.EndUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EndUserController {
    private final EndUserService endUserService;

    public EndUserController(EndUserService endUserService) {
        this.endUserService = endUserService;
    }

    @GetMapping("/users")
    public List<EndUserDTO> getAllUsers() {
        return endUserService.getAllUsers();
    }
}
