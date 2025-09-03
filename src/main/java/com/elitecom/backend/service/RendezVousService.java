package com.elitecom.backend.service;

import com.elitecom.backend.entity.RendezVous;
import com.elitecom.backend.entity.Client;
import com.elitecom.backend.entity.Practitioner;
import com.elitecom.backend.repository.RendezVousRepository;
import com.elitecom.backend.repository.ClientRepository;
import com.elitecom.backend.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public Optional<RendezVous> getRendezVousById(Long id) {
        return rendezVousRepository.findById(id);
    }

    public List<RendezVous> getRendezVousByClient(Long clientId) {
        return rendezVousRepository.findByClientId(clientId);
    }

    public List<RendezVous> getRendezVousByPractitioner(Long practitionerId) {
        return rendezVousRepository.findByPractitionerId(practitionerId);
    }

    public List<RendezVous> getRendezVousByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rendezVousRepository.findByDateHeureDebutBetween(startDate, endDate);
    }

    public List<RendezVous> getRendezVousByStatus(String status) {
        return rendezVousRepository.findByStatut(status);
    }

    public RendezVous createRendezVous(RendezVous rendezVous) {
        // Validate input parameters
        if (rendezVous == null || rendezVous.getClient() == null || 
            rendezVous.getPractitioner() == null || rendezVous.getDateHeureDebut() == null) {
            return null;
        }
        
        // Validate that client and practitioner exist
        Optional<Client> clientOpt = clientRepository.findById(rendezVous.getClient().getId());
        Optional<Practitioner> practitionerOpt = practitionerRepository.findById(rendezVous.getPractitioner().getId());
        
        if (clientOpt.isPresent() && practitionerOpt.isPresent()) {
            // Check if the time slot is available
            if (!isTimeSlotAvailable(rendezVous.getPractitioner().getId(), rendezVous.getDateHeureDebut(), null)) {
                return null; // Time slot not available
            }
            
            rendezVous.setClient(clientOpt.get());
            rendezVous.setPractitioner(practitionerOpt.get());
            rendezVous.setCreatedAt(LocalDateTime.now());
            return rendezVousRepository.save(rendezVous);
        }
        return null;
    }

    public RendezVous updateRendezVous(Long id, RendezVous rendezVousDetails) {
        Optional<RendezVous> rendezVousOpt = rendezVousRepository.findById(id);
        if (rendezVousOpt.isPresent()) {
            RendezVous rendezVous = rendezVousOpt.get();
            
            // Check if the new time slot is available (excluding this appointment)
            if (!isTimeSlotAvailable(rendezVous.getPractitioner().getId(), rendezVousDetails.getDateHeureDebut(), id)) {
                return null; // Time slot not available
            }
            
            rendezVous.setDateHeureDebut(rendezVousDetails.getDateHeureDebut());
            rendezVous.setType(rendezVousDetails.getType());
            rendezVous.setStatut(rendezVousDetails.getStatut());
            
            return rendezVousRepository.save(rendezVous);
        }
        return null;
    }

    public RendezVous updateStatus(Long id, String status) {
        Optional<RendezVous> rendezVousOpt = rendezVousRepository.findById(id);
        if (rendezVousOpt.isPresent()) {
            RendezVous rendezVous = rendezVousOpt.get();
            rendezVous.setStatut(status);
            return rendezVousRepository.save(rendezVous);
        }
        return null;
    }

    public boolean deleteRendezVous(Long id) {
        if (rendezVousRepository.existsById(id)) {
            rendezVousRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RendezVous> getUpcomingRendezVous(Long userId, String userType) {
        if (userId == null || userType == null) {
            return List.of();
        }
        
        LocalDateTime now = LocalDateTime.now();
        if ("client".equalsIgnoreCase(userType)) {
            Optional<Client> clientOpt = clientRepository.findByUserId(userId);
            if (clientOpt.isPresent()) {
                return rendezVousRepository.findByClientIdAndDateHeureDebutAfterOrderByDateHeureDebutAsc(
                    clientOpt.get().getId(), now);
            }
        } else if ("practitioner".equalsIgnoreCase(userType)) {
            Optional<Practitioner> practitionerOpt = practitionerRepository.findByUserId(userId);
            if (practitionerOpt.isPresent()) {
                return rendezVousRepository.findByPractitionerIdAndDateHeureDebutAfterOrderByDateHeureDebutAsc(
                    practitionerOpt.get().getId(), now);
            }
        }
        return List.of();
    }

    public boolean isTimeSlotAvailable(Long practitionerId, LocalDateTime dateTime, Long excludeRendezVousId) {
        // Validate input parameters
        if (practitionerId == null || dateTime == null) {
            return false;
        }
        
        // Check if the time slot is available for the practitioner
        // We'll assume appointments are 1 hour long for now
        LocalDateTime endTime = dateTime.plusHours(1);
        
        List<RendezVous> conflictingRendezVous = rendezVousRepository.findByPractitionerIdAndDateHeureDebutBetween(
            practitionerId, dateTime, endTime);
        
        // If we're updating an existing appointment, exclude it from the conflict check
        if (excludeRendezVousId != null) {
            conflictingRendezVous = conflictingRendezVous.stream()
                .filter(rv -> !rv.getId().equals(excludeRendezVousId))
                .toList();
        }
        
        return conflictingRendezVous.isEmpty();
    }
}
