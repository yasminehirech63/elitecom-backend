package com.elitecom.backend.controller;

import com.elitecom.backend.dto.ValidationRequest;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.entity.ValidationStatus;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Get all practitioners pending validation
    @GetMapping("/practitioners/pending")
    public ResponseEntity<List<User>> getPendingPractitioners() {
        List<User> pendingPractitioners = userRepository.findPractitionersByValidationStatus(ValidationStatus.PENDING);
        return ResponseEntity.ok(pendingPractitioners);
    }

    // Get practitioners by validation status
    @GetMapping("/practitioners/status/{status}")
    public ResponseEntity<List<User>> getPractitionersByStatus(@PathVariable String status) {
        try {
            ValidationStatus validationStatus = ValidationStatus.valueOf(status.toUpperCase());
            List<User> practitioners = userRepository.findPractitionersByValidationStatus(validationStatus);
            return ResponseEntity.ok(practitioners);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Validate/Reject practitioner
    @PostMapping("/validate-practitioner")
    public ResponseEntity<?> validatePractitioner(@Valid @RequestBody ValidationRequest request) {
        try {
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Utilisateur non trouvé"));
            }

            User user = userOpt.get();
            if (user.getRole() != Role.PRACTITIONER) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "L'utilisateur n'est pas un praticien"));
            }

            // Set validation status
            ValidationStatus status = ValidationStatus.valueOf(request.getValidationStatus().toUpperCase());
            user.setValidationStatus(status);
            user.setValidationDate(LocalDateTime.now());
            user.setValidationNotes(request.getValidationNotes());

            // If approved, activate the user
            if (status == ValidationStatus.APPROVED) {
                user.setActive(true);
                user.setVerified(true);
            } else if (status == ValidationStatus.REJECTED) {
                user.setActive(false);
            }

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                "message", "Statut de validation mis à jour avec succès",
                "userId", savedUser.getId(),
                "status", savedUser.getValidationStatus().toString()
            ));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Statut de validation invalide"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la validation: " + e.getMessage()));
        }
    }

    // Get validation statistics
    @GetMapping("/stats/validation")
    public ResponseEntity<Map<String, Object>> getValidationStats() {
        Long pendingCount = userRepository.countPendingPractitioners();
        List<User> approvedPractitioners = userRepository.findPractitionersByValidationStatus(ValidationStatus.APPROVED);
        List<User> rejectedPractitioners = userRepository.findPractitionersByValidationStatus(ValidationStatus.REJECTED);

        Map<String, Object> stats = Map.of(
            "pendingPractitioners", pendingCount,
            "approvedPractitioners", approvedPractitioners.size(),
            "rejectedPractitioners", rejectedPractitioners.size(),
            "totalPractitioners", pendingCount + approvedPractitioners.size() + rejectedPractitioners.size()
        );

        return ResponseEntity.ok(stats);
    }

    // Get all users for admin dashboard
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Get users by role
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        try {
            Role userRole = Role.valueOf(role.toUpperCase());
            List<User> users = userRepository.findByRole(userRole);
            return ResponseEntity.ok(users);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update user status (activate/deactivate)
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long userId, @RequestParam boolean active) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.setActive(active);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "message", "Statut utilisateur mis à jour",
            "userId", user.getId(),
            "active", user.isActive()
        ));
    }
}
