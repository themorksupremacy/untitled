package org.example.dto;

public class LoginResponse {
    private String message;
    private String token;
    private long userId;

    // Constructors, getters, and setters
    public LoginResponse() {}

    public LoginResponse(String message, String token, long userId) {
        this.message = message;
        this.token = token;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
