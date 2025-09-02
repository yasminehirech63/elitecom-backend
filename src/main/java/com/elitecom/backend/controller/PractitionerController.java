package com.elitecom.backend.controller;

import com.elitecom.backend.dto.PractitionerRegistrationRequest;
import com.elitecom.backend.dto.RegistrationResponse;
import com.elitecom.backend.entity.Practitioner;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.entity.ValidationStatus;
import com.elitecom.backend.repository.PractitionerRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/practitioners")
@CrossOrigin(origins = "*")
public class PractitionerController {

    @Autowired
    private PractitionerRepository practitionerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerPractitioner(@Valid @RequestBody PractitionerRegistrationRequest request) {
        try {
            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Un utilisateur avec cet email existe déjà", false));
            }

            // Create user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(Role.PRACTITIONER);
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            user.setSpecialization(request.getSpecialite());
            // Set validation status to PENDING for practitioners
            user.setValidationStatus(ValidationStatus.PENDING);
            
            User savedUser = userRepository.save(user);

            // Create practitioner
            Practitioner practitioner = new Practitioner(
                request.getSpecialite(),
                request.getServicesOfferts(),
                request.getDocuments(),
                request.getProfessionalCardNumber(),
                request.getProfessionalCardFile(),
                savedUser
            );
            practitionerRepository.save(practitioner);

            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getCreatedAt(),
                savedUser.isVerified()
            );

            return ResponseEntity.ok(new RegistrationResponse("Inscription praticien réussie! En attente de validation par l'administrateur.", userInfo));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse("Erreur lors de l'inscription: " + e.getMessage(), false));
        }
    }

    // GET all practitioners
    @GetMapping
    public ResponseEntity<List<Practitioner>> getAllPractitioners() {
        return ResponseEntity.ok(practitionerRepository.findAll());
    }

    // GET practitioner by ID
    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitionerById(@PathVariable Long id) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        return practitioner.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // GET practitioner by email
    @GetMapping("/by-email/{email}")
    public ResponseEntity<Practitioner> getPractitionerByEmail(@PathVariable String email) {
        Optional<Practitioner> practitioner = practitionerRepository.findByUserEmail(email);
        return practitioner.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    // GET practitioners by specialite
    @GetMapping("/by-specialite/{specialite}")
    public ResponseEntity<List<Practitioner>> getPractitionersBySpecialite(@PathVariable String specialite) {
        List<Practitioner> practitioners = practitionerRepository.findBySpecialite(specialite);
        return ResponseEntity.ok(practitioners);
    }

    // GET practitioners by specialite search
    @GetMapping("/search/{specialite}")
    public ResponseEntity<List<Practitioner>> searchPractitionersBySpecialite(@PathVariable String specialite) {
        List<Practitioner> practitioners = practitionerRepository.findBySpecialiteContaining(specialite);
        return ResponseEntity.ok(practitioners);
    }

    // PUT update practitioner
    @PutMapping("/{id}")
    public ResponseEntity<Practitioner> updatePractitioner(@PathVariable Long id, @Valid @RequestBody PractitionerRegistrationRequest request) {
        Optional<Practitioner> existingPractitioner = practitionerRepository.findById(id);
        if (existingPractitioner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Practitioner practitioner = existingPractitioner.get();
        User user = practitioner.getUser();

        // Update user information
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setSpecialization(request.getSpecialite());
        
        // Update practitioner information
        practitioner.setSpecialite(request.getSpecialite());
        practitioner.setServicesOfferts(request.getServicesOfferts());
        practitioner.setDocuments(request.getDocuments());

        userRepository.save(user);
        practitionerRepository.save(practitioner);

        return ResponseEntity.ok(practitioner);
    }

    // DELETE practitioner
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractitioner(@PathVariable Long id) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        if (practitioner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Delete the associated user first
        User user = practitioner.get().getUser();
        practitionerRepository.deleteById(id);
        userRepository.deleteById(user.getId());

        return ResponseEntity.noContent().build();
    }

    // PATCH update practitioner status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Practitioner> updatePractitionerStatus(@PathVariable Long id, @RequestParam boolean active) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        if (practitioner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = practitioner.get().getUser();
        user.setActive(active);
        userRepository.save(user);

        return ResponseEntity.ok(practitioner.get());
    }

    // PATCH update practitioner specialite
    @PatchMapping("/{id}/specialite")
    public ResponseEntity<Practitioner> updatePractitionerSpecialite(@PathVariable Long id, @RequestParam String specialite) {
        Optional<Practitioner> practitioner = practitionerRepository.findById(id);
        if (practitioner.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        practitioner.get().setSpecialite(specialite);
        Practitioner savedPractitioner = practitionerRepository.save(practitioner.get());

        return ResponseEntity.ok(savedPractitioner);
    }
}
