package com.elitecom.backend.repository;

import com.elitecom.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUtilisateurId(Long utilisateurId);
    
    @Query("SELECT n FROM Notification n WHERE n.utilisateur.id = :userId AND n.lu = false ORDER BY n.dateTimeEnvoi DESC")
    List<Notification> findUnreadByUserId(@Param("userId") Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.utilisateur.id = :userId ORDER BY n.dateTimeEnvoi DESC")
    List<Notification> findByUserIdOrderByDateDesc(@Param("userId") Long userId);
    
    long countByUtilisateurIdAndLuFalse(Long utilisateurId);
}
