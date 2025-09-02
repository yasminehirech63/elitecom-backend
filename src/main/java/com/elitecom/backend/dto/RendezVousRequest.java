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
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Practician ID is required")
    private Long practicianId;
    
    private String statut = "PENDING";
    
    // Constructors
    public RendezVousRequest() {}
    
    public RendezVousRequest(LocalDateTime dateHeureDebut, String type, Long patientId, Long practicianId) {
        this.dateHeureDebut = dateHeureDebut;
        this.type = type;
        this.patientId = patientId;
        this.practicianId = practicianId;
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
    
    public Long getPatientId() {
        return patientId;
    }
    
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
    
    public Long getPracticianId() {
        return practicianId;
    }
    
    public void setPracticianId(Long practicianId) {
        this.practicianId = practicianId;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
}
