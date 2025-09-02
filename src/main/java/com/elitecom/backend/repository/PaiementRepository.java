package com.elitecom.backend.repository;

import com.elitecom.backend.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    
    List<Paiement> findByPatientId(Long patientId);
    
    List<Paiement> findByStatut(String statut);
    
    @Query("SELECT p FROM Paiement p WHERE p.datePaiement BETWEEN :startDate AND :endDate")
    List<Paiement> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.montant) FROM Paiement p WHERE p.patient.id = :patientId AND p.statut = 'COMPLETED'")
    Double getTotalPaidByPatient(@Param("patientId") Long patientId);
}
