package org.example.controller;

import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.service.EndUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {
    private final EndUserService endUserService;

    public LoginController(EndUserService endUserService) {
        this.endUserService = endUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = endUserService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(new LoginResponse("Login successful", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new LoginResponse(e.getMessage(), null));
        }
    }
}

