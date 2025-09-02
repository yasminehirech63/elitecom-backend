package com.elitecom.backend.repository;

import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.entity.ValidationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByDomain(String domain);
    boolean existsByEmail(String email);
    
    // Validation queries
    List<User> findByRoleAndValidationStatus(Role role, ValidationStatus validationStatus);
    List<User> findByValidationStatus(ValidationStatus validationStatus);
    
    @Query("SELECT u FROM User u WHERE u.role = 'PRACTITIONER' AND u.validationStatus = :status")
    List<User> findPractitionersByValidationStatus(@Param("status") ValidationStatus status);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'PRACTITIONER' AND u.validationStatus = 'PENDING'")
    Long countPendingPractitioners();
}
