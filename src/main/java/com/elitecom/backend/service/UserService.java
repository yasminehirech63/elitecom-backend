package com.elitecom.backend.service;

import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.entity.ValidationStatus;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getPractitionersByValidationStatus(ValidationStatus status) {
        return userRepository.findPractitionersByValidationStatus(status);
    }

    public Long getPendingPractitionersCount() {
        return userRepository.countPendingPractitioners();
    }

    public User createUser(User user) {
        // Encode password if not already encoded
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User updateUserStatus(Long userId, boolean active) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(active);
            return userRepository.save(user);
        }
        return null;
    }

    public User updateValidationStatus(Long userId, ValidationStatus status, String notes) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setValidationStatus(status);
            user.setValidationDate(LocalDateTime.now());
            user.setValidationNotes(notes);
            
            if (status == ValidationStatus.APPROVED) {
                user.setActive(true);
                user.setVerified(true);
            } else if (status == ValidationStatus.REJECTED) {
                user.setActive(false);
            }
            
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
