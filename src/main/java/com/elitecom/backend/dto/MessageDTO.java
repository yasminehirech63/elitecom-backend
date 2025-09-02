package com.elitecom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MessageDTO {
    
    private Long id;
    
    @NotBlank(message = "Message content is required")
    private String contenu;
    
    private LocalDateTime dateTimeEnvoi;
    
    @NotNull(message = "Sender ID is required")
    private Long emetteurId;
    
    @NotNull(message = "Receiver ID is required")
    private Long recepteurId;
    
    private String emetteurNom;
    private String recepteurNom;
    
    // Constructors
    public MessageDTO() {}
    
    public MessageDTO(String contenu, Long emetteurId, Long recepteurId) {
        this.contenu = contenu;
        this.emetteurId = emetteurId;
        this.recepteurId = recepteurId;
        this.dateTimeEnvoi = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContenu() {
        return contenu;
    }
    
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
    
    public LocalDateTime getDateTimeEnvoi() {
        return dateTimeEnvoi;
    }
    
    public void setDateTimeEnvoi(LocalDateTime dateTimeEnvoi) {
        this.dateTimeEnvoi = dateTimeEnvoi;
    }
    
    public Long getEmetteurId() {
        return emetteurId;
    }
    
    public void setEmetteurId(Long emetteurId) {
        this.emetteurId = emetteurId;
    }
    
    public Long getRecepteurId() {
        return recepteurId;
    }
    
    public void setRecepteurId(Long recepteurId) {
        this.recepteurId = recepteurId;
    }
    
    public String getEmetteurNom() {
        return emetteurNom;
    }
    
    public void setEmetteurNom(String emetteurNom) {
        this.emetteurNom = emetteurNom;
    }
    
    public String getRecepteurNom() {
        return recepteurNom;
    }
    
    public void setRecepteurNom(String recepteurNom) {
        this.recepteurNom = recepteurNom;
    }
}
