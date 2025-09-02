package com.elitecom.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class RendezVousRequest {
    
    @NotNull(message = "Date and time are required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime dateHeureDebut;
    
    @NotBlank(message = "Type is required")
    private String type;
    
    @NotNull(message = "Client ID is required")
    private Long clientId;
    
    @NotNull(message = "Practitioner ID is required")
    private Long practitionerId;
    
    private String statut = "PENDING";
    
    // Constructors
    public RendezVousRequest() {}
    
    public RendezVousRequest(LocalDateTime dateHeureDebut, String type, Long clientId, Long practitionerId) {
        this.dateHeureDebut = dateHeureDebut;
        this.type = type;
        this.clientId = clientId;
        this.practitionerId = practitionerId;
    }
    
    // Getters and Setters
    public LocalDateTime getDateHeureDebut() {
        return dateHeureDebut;
    }
    
    public void setDateHeureDebut(LocalDateTime dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getClientId() {
        return clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Long getPractitionerId() {
        return practitionerId;
    }
    
    public void setPractitionerId(Long practitionerId) {
        this.practitionerId = practitionerId;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
}
