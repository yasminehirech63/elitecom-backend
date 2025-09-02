package com.elitecom.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "date_time_envoi", nullable = false)
    private LocalDateTime dateTimeEnvoi;
    
    @Column(nullable = false)
    private Boolean lu = false;
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Notification() {}
    
    public Notification(String message, LocalDateTime dateTimeEnvoi, User utilisateur) {
        this.message = message;
        this.dateTimeEnvoi = dateTimeEnvoi;
        this.utilisateur = utilisateur;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getDateTimeEnvoi() {
        return dateTimeEnvoi;
    }
    
    public void setDateTimeEnvoi(LocalDateTime dateTimeEnvoi) {
        this.dateTimeEnvoi = dateTimeEnvoi;
    }
    
    public Boolean getLu() {
        return lu;
    }
    
    public void setLu(Boolean lu) {
        this.lu = lu;
    }
    
    public User getUtilisateur() {
        return utilisateur;
    }
    
    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
