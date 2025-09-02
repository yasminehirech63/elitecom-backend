package com.elitecom.backend.repository;

import com.elitecom.backend.entity.Practician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PracticianRepository extends JpaRepository<Practician, Long> {
    
    Optional<Practician> findByUserId(Long userId);
    
    @Query("SELECT p FROM Practician p WHERE p.user.email = :email")
    Optional<Practician> findByUserEmail(@Param("email") String email);
    
    List<Practician> findBySpecialite(String specialite);
    
    @Query("SELECT p FROM Practician p WHERE p.specialite LIKE %:specialite%")
    List<Practician> findBySpecialiteContaining(@Param("specialite") String specialite);
}
