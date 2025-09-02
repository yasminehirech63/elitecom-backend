package com.elitecom.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;
    
    @Column(name = "date_time_envoi", nullable = false)
    private LocalDateTime dateTimeEnvoi;
    
    @ManyToOne
    @JoinColumn(name = "emetteur_id", nullable = false)
    private User emetteur;
    
    @ManyToOne
    @JoinColumn(name = "recepteur_id", nullable = false)
    private User recepteur;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Message() {}
    
    public Message(String contenu, LocalDateTime dateTimeEnvoi, User emetteur, User recepteur) {
        this.contenu = contenu;
        this.dateTimeEnvoi = dateTimeEnvoi;
        this.emetteur = emetteur;
        this.recepteur = recepteur;
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
    
    public User getEmetteur() {
        return emetteur;
    }
    
    public void setEmetteur(User emetteur) {
        this.emetteur = emetteur;
    }
    
    public User getRecepteur() {
        return recepteur;
    }
    
    public void setRecepteur(User recepteur) {
        this.recepteur = recepteur;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
