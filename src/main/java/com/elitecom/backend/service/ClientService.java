package com.elitecom.backend.service;

import com.elitecom.backend.entity.Client;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.repository.ClientRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Optional<Client> getClientByCin(String cin) {
        return clientRepository.findByCin(cin);
    }

    public Optional<Client> getClientByEmail(String email) {
        return clientRepository.findByUserEmail(email);
    }

    public Client createClient(Client client) {
        // Ensure the associated user is saved first
        User user = client.getUser();
        if (user.getId() == null) {
            user.setRole(Role.CLIENT);
            user.setCreatedAt(LocalDateTime.now());
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user = userRepository.save(user);
            client.setUser(user);
        }
        
        client.setCreatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client clientDetails) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            
            // Update user information
            User user = client.getUser();
            user.setFirstName(clientDetails.getUser().getFirstName());
            user.setLastName(clientDetails.getUser().getLastName());
            user.setPhone(clientDetails.getUser().getPhone());
            user.setAddress(clientDetails.getUser().getAddress());
            userRepository.save(user);
            
            // Update client information
            client.setCin(clientDetails.getCin());
            return clientRepository.save(client);
        }
        return null;
    }

    public boolean deleteClient(Long id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            User user = client.getUser();
            
            // Delete client first, then user
            clientRepository.deleteById(id);
            userRepository.deleteById(user.getId());
            return true;
        }
        return false;
    }

    public Client updateClientStatus(Long id, boolean active) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            User user = client.getUser();
            user.setActive(active);
            userRepository.save(user);
            return client;
        }
        return null;
    }

    public boolean cinExists(String cin) {
        return clientRepository.existsByCin(cin);
    }
}
