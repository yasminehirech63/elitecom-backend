package com.elitecom.backend.dto;

import jakarta.validation.constraints.NotNull;

public class ValidationRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Validation status is required")
    private String validationStatus;
    
    private String validationNotes;
    
    // Constructors
    public ValidationRequest() {}
    
    public ValidationRequest(Long userId, String validationStatus, String validationNotes) {
        this.userId = userId;
        this.validationStatus = validationStatus;
        this.validationNotes = validationNotes;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getValidationStatus() {
        return validationStatus;
    }
    
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }
    
    public String getValidationNotes() {
        return validationNotes;
    }
    
    public void setValidationNotes(String validationNotes) {
        this.validationNotes = validationNotes;
    }
}
