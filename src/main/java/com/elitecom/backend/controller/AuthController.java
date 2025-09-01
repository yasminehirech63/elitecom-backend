package com.elitecom.backend.controller;

import com.elitecom.backend.dto.RegistrationRequest;
import com.elitecom.backend.dto.RegistrationResponse;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email and password are required"));
            }

            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid credentials"));
            }

            User user = userOpt.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid credentials"));
            }

            // Create response with user info
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", user.getRole().toString()
            ));
            response.put("token", "jwt-token-placeholder"); // You can implement JWT later

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegistrationRequest registerRequest) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("User with this email already exists", false));
            }

            // Create new user
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setRole(Role.valueOf(registerRequest.getRole().toUpperCase()));
            
            // Set optional fields if provided
            if (registerRequest.getPhone() != null) {
                user.setPhone(registerRequest.getPhone());
            }
            if (registerRequest.getAddress() != null) {
                user.setAddress(registerRequest.getAddress());
            }
            if (registerRequest.getDomain() != null) {
                user.setDomain(registerRequest.getDomain());
            }
            if (registerRequest.getSpecialization() != null) {
                user.setSpecialization(registerRequest.getSpecialization());
            }

            User savedUser = userRepository.save(user);

            // Create user info for response
            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getCreatedAt(),
                savedUser.isVerified()
            );

            RegistrationResponse response = new RegistrationResponse("Registration successful! Welcome to EliteCom platform.", userInfo);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new RegistrationResponse("Invalid role specified", false));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse("Registration failed: " + e.getMessage(), false));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of("message", "Auth controller is working!"));
    }
}
