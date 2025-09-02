package com.elitecom.backend.controller;

import com.elitecom.backend.dto.RendezVousRequest;
import com.elitecom.backend.entity.RendezVous;
import com.elitecom.backend.entity.Client;
import com.elitecom.backend.entity.Practitioner;
import com.elitecom.backend.repository.RendezVousRepository;
import com.elitecom.backend.repository.ClientRepository;
import com.elitecom.backend.repository.PractitionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/rendez-vous")
@CrossOrigin(origins = "*")
public class RendezVousController {

    @Autowired
    private RendezVousRepository rendezVousRepository;
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private PractitionerRepository practitionerRepository;

    @PostMapping
    public ResponseEntity<?> createRendezVous(@Valid @RequestBody RendezVousRequest request) {
        try {
            Optional<Client> client = clientRepository.findById(request.getClientId());
            Optional<Practitioner> practitioner = practitionerRepository.findById(request.getPractitionerId());
            
            if (client.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Client non trouvé"));
            }
            
            if (practitioner.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Praticien non trouvé"));
            }

            RendezVous rendezVous = new RendezVous(
                request.getDateHeureDebut(),
                request.getType(),
                request.getStatut(),
                client.get(),
                practitioner.get()
            );

            RendezVous savedRendezVous = rendezVousRepository.save(rendezVous);
            return ResponseEntity.ok(savedRendezVous);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la création du rendez-vous: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<RendezVous>> getAllRendezVous() {
        return ResponseEntity.ok(rendezVousRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getRendezVousById(@PathVariable Long id) {
        Optional<RendezVous> rendezVous = rendezVousRepository.findById(id);
        return rendezVous.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByClient(@PathVariable Long clientId) {
        List<RendezVous> rendezVous = rendezVousRepository.findByClientId(clientId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/practitioner/{practitionerId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByPractitioner(@PathVariable Long practitionerId) {
        List<RendezVous> rendezVous = rendezVousRepository.findByPractitionerId(practitionerId);
        return ResponseEntity.ok(rendezVous);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        try {
            Optional<RendezVous> rendezVousOpt = rendezVousRepository.findById(id);
            if (rendezVousOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            RendezVous rendezVous = rendezVousOpt.get();
            rendezVous.setStatut(statusUpdate.get("statut"));
            RendezVous updated = rendezVousRepository.save(rendezVous);
            
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de la mise à jour: " + e.getMessage()));
        }
    }
}
