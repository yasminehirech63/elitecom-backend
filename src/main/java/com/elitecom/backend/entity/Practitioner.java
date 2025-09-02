package com.elitecom.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "practitioners")
public class Practitioner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String specialite;
    
    @Column(name = "services_offerts")
    private String servicesOfferts;
    
    @Column(nullable = false)
    private String documents;
    
    // Professional card fields
    @Column(name = "professional_card_number")
    private String professionalCardNumber;
    
    @Column(name = "professional_card_file")
    private String professionalCardFile;
    
    @Column(name = "professional_card_upload_date")
    private LocalDateTime professionalCardUploadDate;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "practitioner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RendezVous> rendezVous;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Practitioner() {}
    
    public Practitioner(String specialite, String servicesOfferts, String documents, User user) {
        this.specialite = specialite;
        this.servicesOfferts = servicesOfferts;
        this.documents = documents;
        this.user = user;
    }
    
    public Practitioner(String specialite, String servicesOfferts, String documents, 
                       String professionalCardNumber, String professionalCardFile, User user) {
        this.specialite = specialite;
        this.servicesOfferts = servicesOfferts;
        this.documents = documents;
        this.professionalCardNumber = professionalCardNumber;
        this.professionalCardFile = professionalCardFile;
        this.professionalCardUploadDate = LocalDateTime.now();
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getProfessionalCardUploadDate() {
        return professionalCardUploadDate;
    }
    
    public void setProfessionalCardUploadDate(LocalDateTime professionalCardUploadDate) {
        this.professionalCardUploadDate = professionalCardUploadDate;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<RendezVous> getRendezVous() {
        return rendezVous;
    }
    
    public void setRendezVous(List<RendezVous> rendezVous) {
        this.rendezVous = rendezVous;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
