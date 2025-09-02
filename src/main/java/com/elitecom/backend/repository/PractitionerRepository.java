package com.elitecom.backend.repository;

import com.elitecom.backend.entity.Practitioner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PractitionerRepository extends JpaRepository<Practitioner, Long> {
    
    Optional<Practitioner> findByUserId(Long userId);
    
    @Query("SELECT p FROM Practitioner p WHERE p.user.email = :email")
    Optional<Practitioner> findByUserEmail(@Param("email") String email);
    
    List<Practitioner> findBySpecialite(String specialite);
    
    @Query("SELECT p FROM Practitioner p WHERE p.specialite LIKE %:specialite%")
    List<Practitioner> findBySpecialiteContaining(@Param("specialite") String specialite);
}
