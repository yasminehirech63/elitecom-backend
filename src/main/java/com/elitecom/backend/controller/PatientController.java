package com.elitecom.backend.controller;

import com.elitecom.backend.dto.PatientRegistrationRequest;
import com.elitecom.backend.dto.RegistrationResponse;
import com.elitecom.backend.entity.Patient;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.repository.PatientRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerPatient(@Valid @RequestBody PatientRegistrationRequest request) {
        try {
            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Un utilisateur avec cet email existe déjà", false));
            }
            
            // Check if CIN already exists
            if (patientRepository.existsByCin(request.getCin())) {
                return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Un patient avec ce CIN existe déjà", false));
            }

            // Create user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(Role.PATIENT);
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            
            User savedUser = userRepository.save(user);

            // Create patient
            Patient patient = new Patient(request.getCin(), savedUser);
            Patient savedPatient = patientRepository.save(patient);

            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getCreatedAt(),
                savedUser.isVerified()
            );

            return ResponseEntity.ok(new RegistrationResponse("Inscription patient réussie!", userInfo));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse("Erreur lors de l'inscription: " + e.getMessage(), false));
        }
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = patientRepository.findByUserEmail(email);
        return patient.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
}
