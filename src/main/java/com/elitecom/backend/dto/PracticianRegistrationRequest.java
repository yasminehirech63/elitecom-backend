package com.elitecom.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PracticianRegistrationRequest extends RegistrationRequest {
    
    @NotBlank(message = "Specialite is required for practician registration")
    private String specialite;
    
    private String servicesOfferts;
    
    @NotBlank(message = "Documents are required for practician registration")
    private String documents;
    
    // Constructors
    public PracticianRegistrationRequest() {
        super();
        this.setRole("PRACTICIAN");
    }
    
    public PracticianRegistrationRequest(String firstName, String lastName, String email, String password, 
                                       String specialite, String servicesOfferts, String documents) {
        super(firstName, lastName, email, password);
        this.specialite = specialite;
        this.servicesOfferts = servicesOfferts;
        this.documents = documents;
        this.setRole("PRACTICIAN");
    }
    
    // Getters and Setters
    public String getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    public String getServicesOfferts() {
        return servicesOfferts;
    }
    
    public void setServicesOfferts(String servicesOfferts) {
        this.servicesOfferts = servicesOfferts;
    }
    
    public String getDocuments() {
        return documents;
    }
    
    public void setDocuments(String documents) {
        this.documents = documents;
    }
}
