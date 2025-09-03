package com.elitecom.backend.service;

import com.elitecom.backend.entity.Practitioner;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.repository.PractitionerRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PractitionerService {

    @Autowired
    private PractitionerRepository practitionerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Practitioner> getAllPractitioners() {
        return practitionerRepository.findAll();
    }

    public Optional<Practitioner> getPractitionerById(Long id) {
        return practitionerRepository.findById(id);
    }

    public Optional<Practitioner> getPractitionerByUserId(Long userId) {
        return practitionerRepository.findByUserId(userId);
    }

    public List<Practitioner> getPractitionersBySpecialite(String specialite) {
        return practitionerRepository.findBySpecialite(specialite);
    }

    public Practitioner createPractitioner(Practitioner practitioner) {
        // Ensure the associated user is saved first
        User user = practitioner.getUser();
        if (user.getId() == null) {
            user.setRole(Role.PRACTITIONER);
            user.setCreatedAt(LocalDateTime.now());
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user = userRepository.save(user);
            practitioner.setUser(user);
        }
        
        practitioner.setCreatedAt(LocalDateTime.now());
        return practitionerRepository.save(practitioner);
    }

    public Practitioner updatePractitioner(Long id, Practitioner practitionerDetails) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            
            // Update user information
            User user = practitioner.getUser();
            user.setFirstName(practitionerDetails.getUser().getFirstName());
            user.setLastName(practitionerDetails.getUser().getLastName());
            user.setPhone(practitionerDetails.getUser().getPhone());
            user.setAddress(practitionerDetails.getUser().getAddress());
            user.setDomain(practitionerDetails.getUser().getDomain());
            user.setSpecialization(practitionerDetails.getUser().getSpecialization());
            userRepository.save(user);
            
            // Update practitioner information
            practitioner.setSpecialite(practitionerDetails.getSpecialite());
            practitioner.setServicesOfferts(practitionerDetails.getServicesOfferts());
            practitioner.setDocuments(practitionerDetails.getDocuments());
            practitioner.setProfessionalCardNumber(practitionerDetails.getProfessionalCardNumber());
            practitioner.setProfessionalCardFile(practitionerDetails.getProfessionalCardFile());
            practitioner.setProfessionalCardUploadDate(practitionerDetails.getProfessionalCardUploadDate());
            
            return practitionerRepository.save(practitioner);
        }
        return null;
    }

    public boolean deletePractitioner(Long id) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            User user = practitioner.getUser();
            
            // Delete practitioner first, then user
            practitionerRepository.deleteById(id);
            userRepository.deleteById(user.getId());
            return true;
        }
        return false;
    }

    public Practitioner updateProfessionalCard(Long id, String cardNumber, String cardFile) {
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(id);
        if (practitionerOpt.isPresent()) {
            Practitioner practitioner = practitionerOpt.get();
            practitioner.setProfessionalCardNumber(cardNumber);
            practitioner.setProfessionalCardFile(cardFile);
            practitioner.setProfessionalCardUploadDate(LocalDateTime.now());
            return practitionerRepository.save(practitioner);
        }
        return null;
    }
}
