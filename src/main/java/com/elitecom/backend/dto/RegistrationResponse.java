package com.elitecom.backend.dto;

import java.time.LocalDateTime;

public class RegistrationResponse {
    
    private String message;
    private boolean success;
    private UserInfo user;
    private String token;
    
    // Default constructor
    public RegistrationResponse() {}
    
    // Constructor for successful registration
    public RegistrationResponse(String message, UserInfo user) {
        this.message = message;
        this.success = true;
        this.user = user;
    }
    
    // Constructor for failed registration
    public RegistrationResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Nested class for user information in response
    public static class UserInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String role;
        private LocalDateTime createdAt;
        private boolean isVerified;
        
        public UserInfo() {}
        
        public UserInfo(Long id, String firstName, String lastName, String email, String role, LocalDateTime createdAt, boolean isVerified) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.role = role;
            this.createdAt = createdAt;
            this.isVerified = isVerified;
        }
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        
        public boolean isVerified() { return isVerified; }
        public void setVerified(boolean verified) { isVerified = verified; }
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public UserInfo getUser() {
        return user;
    }
    
    public void setUser(UserInfo user) {
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
