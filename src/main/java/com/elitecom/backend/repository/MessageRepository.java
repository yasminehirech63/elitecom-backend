package com.elitecom.backend.repository;

import com.elitecom.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findByEmetteurId(Long emetteurId);
    
    List<Message> findByRecepteurId(Long recepteurId);
    
    @Query("SELECT m FROM Message m WHERE (m.emetteur.id = :userId OR m.recepteur.id = :userId) ORDER BY m.dateTimeEnvoi DESC")
    List<Message> findConversationsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT m FROM Message m WHERE (m.emetteur.id = :user1Id AND m.recepteur.id = :user2Id) OR (m.emetteur.id = :user2Id AND m.recepteur.id = :user1Id) ORDER BY m.dateTimeEnvoi ASC")
    List<Message> findConversationBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
