package com.elitecom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRegistrationRequest extends RegistrationRequest {
    
    @NotBlank(message = "CIN is required for client registration")
    @Size(min = 8, max = 20, message = "CIN must be between 8 and 20 characters")
    private String cin;
    
    // Constructors
    public ClientRegistrationRequest() {
        super();
        this.setRole("CLIENT");
    }
    
    public ClientRegistrationRequest(String firstName, String lastName, String email, String password, String cin) {
        super(firstName, lastName, email, password);
        this.cin = cin;
        this.setRole("CLIENT");
    }
    
    // Getters and Setters
    public String getCin() {
        return cin;
    }
    
    public void setCin(String cin) {
        this.cin = cin;
    }
}
