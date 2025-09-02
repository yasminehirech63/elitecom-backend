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
    
    List<RendezVous> findByPatientId(Long patientId);
    
    List<RendezVous> findByPracticianId(Long practicianId);
    
    List<RendezVous> findByStatut(String statut);
    
    @Query("SELECT r FROM RendezVous r WHERE r.patient.id = :patientId AND r.statut = :statut")
    List<RendezVous> findByPatientIdAndStatut(@Param("patientId") Long patientId, @Param("statut") String statut);
    
    @Query("SELECT r FROM RendezVous r WHERE r.practician.id = :practicianId AND r.statut = :statut")
    List<RendezVous> findByPracticianIdAndStatut(@Param("practicianId") Long practicianId, @Param("statut") String statut);
    
    @Query("SELECT r FROM RendezVous r WHERE r.dateHeureDebut BETWEEN :startDate AND :endDate")
    List<RendezVous> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
