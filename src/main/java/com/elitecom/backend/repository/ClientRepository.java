package com.elitecom.backend.repository;

import com.elitecom.backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByCin(String cin);
    
    Optional<Client> findByUserId(Long userId);
    
    @Query("SELECT c FROM Client c WHERE c.user.email = :email")
    Optional<Client> findByUserEmail(@Param("email") String email);
    
    boolean existsByCin(String cin);
}
