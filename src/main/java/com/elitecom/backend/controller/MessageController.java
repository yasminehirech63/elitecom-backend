package com.elitecom.backend.controller;

import com.elitecom.backend.dto.MessageDTO;
import com.elitecom.backend.entity.Message;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.repository.MessageRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageDTO messageDTO) {
        try {
            Optional<User> emetteur = userRepository.findById(messageDTO.getEmetteurId());
            Optional<User> recepteur = userRepository.findById(messageDTO.getRecepteurId());
            
            if (emetteur.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Émetteur non trouvé"));
            }
            
            if (recepteur.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Récepteur non trouvé"));
            }

            Message message = new Message(
                messageDTO.getContenu(),
                LocalDateTime.now(),
                emetteur.get(),
                recepteur.get()
            );

            Message savedMessage = messageRepository.save(message);
            
            // Convert to DTO for response
            MessageDTO responseDTO = convertToDTO(savedMessage);
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Erreur lors de l'envoi du message: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getUserMessages(@PathVariable Long userId) {
        List<Message> messages = messageRepository.findConversationsByUserId(userId);
        List<MessageDTO> messageDTOs = messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/conversation/{user1Id}/{user2Id}")
    public ResponseEntity<List<MessageDTO>> getConversation(@PathVariable Long user1Id, @PathVariable Long user2Id) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(user1Id, user2Id);
        List<MessageDTO> messageDTOs = messages.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContenu(message.getContenu());
        dto.setDateTimeEnvoi(message.getDateTimeEnvoi());
        dto.setEmetteurId(message.getEmetteur().getId());
        dto.setRecepteurId(message.getRecepteur().getId());
        dto.setEmetteurNom(message.getEmetteur().getFirstName() + " " + message.getEmetteur().getLastName());
        dto.setRecepteurNom(message.getRecepteur().getFirstName() + " " + message.getRecepteur().getLastName());
        return dto;
    }
}
