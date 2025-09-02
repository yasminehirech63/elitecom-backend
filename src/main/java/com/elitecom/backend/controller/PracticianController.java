package com.elitecom.backend.controller;

import com.elitecom.backend.dto.PracticianRegistrationRequest;
import com.elitecom.backend.dto.RegistrationResponse;
import com.elitecom.backend.entity.Practician;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.repository.PracticianRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/practicians")
@CrossOrigin(origins = "*")
public class PracticianController {

    @Autowired
    private PracticianRepository practicianRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerPractician(@Valid @RequestBody PracticianRegistrationRequest request) {
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
            user.setRole(Role.PRACTICIAN);
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            user.setSpecialization(request.getSpecialite());
            
            User savedUser = userRepository.save(user);

            // Create practician
            Practician practician = new Practician(
                request.getSpecialite(),
                request.getServicesOfferts(),
                request.getDocuments(),
                savedUser
            );
            Practician savedPractician = practicianRepository.save(practician);

            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getCreatedAt(),
                savedUser.isVerified()
            );

            return ResponseEntity.ok(new RegistrationResponse("Inscription praticien réussie!", userInfo));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse("Erreur lors de l'inscription: " + e.getMessage(), false));
        }
    }

    @GetMapping
    public ResponseEntity<List<Practician>> getAllPracticians() {
        return ResponseEntity.ok(practicianRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practician> getPracticianById(@PathVariable Long id) {
        Optional<Practician> practician = practicianRepository.findById(id);
        return practician.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-specialite/{specialite}")
    public ResponseEntity<List<Practician>> getPracticiansBySpecialite(@PathVariable String specialite) {
        List<Practician> practicians = practicianRepository.findBySpecialite(specialite);
        return ResponseEntity.ok(practicians);
    }

    @GetMapping("/search/{specialite}")
    public ResponseEntity<List<Practician>> searchPracticiansBySpecialite(@PathVariable String specialite) {
        List<Practician> practicians = practicianRepository.findBySpecialiteContaining(specialite);
        return ResponseEntity.ok(practicians);
    }
}
