package com.elitecom.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class PractitionerRegistrationRequest extends RegistrationRequest {
    
    @NotBlank(message = "Specialite is required for practitioner registration")
    private String specialite;
    
    private String servicesOfferts;
    
    @NotBlank(message = "Documents are required for practitioner registration")
    private String documents;
    
    // Professional card fields
    private String professionalCardNumber;
    private String professionalCardFile;
    
    // Constructors
    public PractitionerRegistrationRequest() {
        super();
        this.setRole("PRACTITIONER");
    }
    
    public PractitionerRegistrationRequest(String firstName, String lastName, String email, String password, 
                                       String specialite, String servicesOfferts, String documents) {
        super(firstName, lastName, email, password);
        this.specialite = specialite;
        this.servicesOfferts = servicesOfferts;
        this.documents = documents;
        this.setRole("PRACTITIONER");
    }
    
    public PractitionerRegistrationRequest(String firstName, String lastName, String email, String password, 
                                       String specialite, String servicesOfferts, String documents,
                                       String professionalCardNumber, String professionalCardFile) {
        super(firstName, lastName, email, password);
        this.specialite = specialite;
        this.servicesOfferts = servicesOfferts;
        this.documents = documents;
        this.professionalCardNumber = professionalCardNumber;
        this.professionalCardFile = professionalCardFile;
        this.setRole("PRACTITIONER");
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
    
    public String getProfessionalCardNumber() {
        return professionalCardNumber;
    }
    
    public void setProfessionalCardNumber(String professionalCardNumber) {
        this.professionalCardNumber = professionalCardNumber;
    }
    
    public String getProfessionalCardFile() {
        return professionalCardFile;
    }
    
    public void setProfessionalCardFile(String professionalCardFile) {
        this.professionalCardFile = professionalCardFile;
    }
}
