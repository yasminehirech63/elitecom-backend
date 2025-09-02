package com.elitecom.backend.controller;

import com.elitecom.backend.dto.RendezVousRequest;
import com.elitecom.backend.entity.RendezVous;
import com.elitecom.backend.entity.Patient;
import com.elitecom.backend.entity.Practician;
import com.elitecom.backend.repository.RendezVousRepository;
import com.elitecom.backend.repository.PatientRepository;
import com.elitecom.backend.repository.PracticianRepository;
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
    private PatientRepository patientRepository;
    
    @Autowired
    private PracticianRepository practicianRepository;

    @PostMapping
    public ResponseEntity<?> createRendezVous(@Valid @RequestBody RendezVousRequest request) {
        try {
            Optional<Patient> patient = patientRepository.findById(request.getPatientId());
            Optional<Practician> practician = practicianRepository.findById(request.getPracticianId());
            
            if (patient.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Patient non trouvé"));
            }
            
            if (practician.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Praticien non trouvé"));
            }

            RendezVous rendezVous = new RendezVous(
                request.getDateHeureDebut(),
                request.getType(),
                request.getStatut(),
                patient.get(),
                practician.get()
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

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByPatient(@PathVariable Long patientId) {
        List<RendezVous> rendezVous = rendezVousRepository.findByPatientId(patientId);
        return ResponseEntity.ok(rendezVous);
    }

    @GetMapping("/practician/{practicianId}")
    public ResponseEntity<List<RendezVous>> getRendezVousByPractician(@PathVariable Long practicianId) {
        List<RendezVous> rendezVous = rendezVousRepository.findByPracticianId(practicianId);
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
