package com.elitecom.backend.controller;

import com.elitecom.backend.dto.ClientRegistrationRequest;
import com.elitecom.backend.dto.RegistrationResponse;
import com.elitecom.backend.entity.Client;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.repository.ClientRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerClient(@Valid @RequestBody ClientRegistrationRequest request) {
        try {
            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Un utilisateur avec cet email existe déjà", false));
            }
            
            // Check if CIN already exists
            if (clientRepository.existsByCin(request.getCin())) {
                return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Un client avec ce CIN existe déjà", false));
            }

            // Create user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setRole(Role.CLIENT);
            user.setPhone(request.getPhone());
            user.setAddress(request.getAddress());
            
            User savedUser = userRepository.save(user);

            // Create client
            Client client = new Client(request.getCin(), savedUser);
            clientRepository.save(client);

            RegistrationResponse.UserInfo userInfo = new RegistrationResponse.UserInfo(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole().toString(),
                savedUser.getCreatedAt(),
                savedUser.isVerified()
            );

            return ResponseEntity.ok(new RegistrationResponse("Inscription client réussie!", userInfo));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new RegistrationResponse("Erreur lors de l'inscription: " + e.getMessage(), false));
        }
    }

    // GET all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientRepository.findAll());
    }

    // GET client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // GET client by email
    @GetMapping("/by-email/{email}")
    public ResponseEntity<Client> getClientByEmail(@PathVariable String email) {
        Optional<Client> client = clientRepository.findByUserEmail(email);
        return client.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // GET client by CIN
    @GetMapping("/by-cin/{cin}")
    public ResponseEntity<Client> getClientByCin(@PathVariable String cin) {
        Optional<Client> client = clientRepository.findByCin(cin);
        return client.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // PUT update client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @Valid @RequestBody ClientRegistrationRequest request) {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (existingClient.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = existingClient.get();
        User user = client.getUser();

        // Update user information
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        
        // Update client information
        client.setCin(request.getCin());

        userRepository.save(user);
        clientRepository.save(client);

        return ResponseEntity.ok(client);
    }

    // DELETE client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Delete the associated user first
        User user = client.get().getUser();
        clientRepository.deleteById(id);
        userRepository.deleteById(user.getId());

        return ResponseEntity.noContent().build();
    }

    // PATCH update client status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Client> updateClientStatus(@PathVariable Long id, @RequestParam boolean active) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = client.get().getUser();
        user.setActive(active);
        userRepository.save(user);

        return ResponseEntity.ok(client.get());
    }
}
