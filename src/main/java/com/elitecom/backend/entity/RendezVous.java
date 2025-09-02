package com.elitecom.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
public class RendezVous {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_heure_debut", nullable = false)
    private LocalDateTime dateHeureDebut;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private String statut;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne
    @JoinColumn(name = "practitioner_id", nullable = false)
    private Practitioner practitioner;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public RendezVous() {}
    
    public RendezVous(LocalDateTime dateHeureDebut, String type, String statut, Client client, Practitioner practitioner) {
        this.dateHeureDebut = dateHeureDebut;
        this.type = type;
        this.statut = statut;
        this.client = client;
        this.practitioner = practitioner;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public Practitioner getPractitioner() {
        return practitioner;
    }
    
    public void setPractitioner(Practitioner practitioner) {
        this.practitioner = practitioner;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
