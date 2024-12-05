package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/test-connection")
    public String testConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Database connection is successful!";
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }

    @GetMapping("/test-users")
    public List<Map<String, Object>> testUsers() {
        try {
            // Query all users
            String query = "SELECT * FROM EndUser";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(query);
            return users; // Returns users as a list of maps
        } catch (Exception e) {
            return List.of(Map.of("error", "Failed to retrieve users: " + e.getMessage()));
        }
    }
}
