package com.elitecom.backend.repository;

import com.elitecom.backend.entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    
    List<RendezVous> findByClientId(Long clientId);
    
    List<RendezVous> findByPractitionerId(Long practitionerId);
    
    List<RendezVous> findByStatut(String statut);
    
    // Date range queries
    List<RendezVous> findByDateHeureDebutBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Upcoming appointments for clients
    List<RendezVous> findByClientIdAndDateHeureDebutAfterOrderByDateHeureDebutAsc(Long clientId, LocalDateTime dateTime);
    
    // Upcoming appointments for practitioners
    List<RendezVous> findByPractitionerIdAndDateHeureDebutAfterOrderByDateHeureDebutAsc(Long practitionerId, LocalDateTime dateTime);
    
    // Time slot availability check
    List<RendezVous> findByPractitionerIdAndDateHeureDebutAndIdNot(Long practitionerId, LocalDateTime dateTime, Long excludeId);
    
    // Check for overlapping appointments
    List<RendezVous> findByPractitionerIdAndDateHeureDebutBetween(Long practitionerId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT r FROM RendezVous r WHERE r.client.id = :clientId AND r.statut = :statut")
    List<RendezVous> findByClientIdAndStatut(@Param("clientId") Long clientId, @Param("statut") String statut);
    
    @Query("SELECT r FROM RendezVous r WHERE r.practitioner.id = :practitionerId AND r.statut = :statut")
    List<RendezVous> findByPractitionerIdAndStatut(@Param("practitionerId") Long practitionerId, @Param("statut") String statut);
    
    @Query("SELECT r FROM RendezVous r WHERE r.dateHeureDebut BETWEEN :startDate AND :endDate")
    List<RendezVous> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
