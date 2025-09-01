package com.elitecom.backend.repository;

import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByDomain(String domain);
    boolean existsByEmail(String email);
}
