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
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "practician_id", nullable = false)
    private Practician practician;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public RendezVous() {}
    
    public RendezVous(LocalDateTime dateHeureDebut, String type, String statut, Patient patient, Practician practician) {
        this.dateHeureDebut = dateHeureDebut;
        this.type = type;
        this.statut = statut;
        this.patient = patient;
        this.practician = practician;
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
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public Practician getPractician() {
        return practician;
    }
    
    public void setPractician(Practician practician) {
        this.practician = practician;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
