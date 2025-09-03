package com.elitecom.backend.service;

import com.elitecom.backend.entity.Message;
import com.elitecom.backend.entity.User;
import com.elitecom.backend.repository.MessageRepository;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesByEmetteur(Long emetteurId) {
        return messageRepository.findByEmetteurId(emetteurId);
    }

    public List<Message> getMessagesByRecepteur(Long recepteurId) {
        return messageRepository.findByRecepteurId(recepteurId);
    }

    public List<Message> getConversationsByUserId(Long userId) {
        return messageRepository.findConversationsByUserId(userId);
    }

    public List<Message> getConversationBetweenUsers(Long user1Id, Long user2Id) {
        return messageRepository.findConversationBetweenUsers(user1Id, user2Id);
    }

    public Message createMessage(Message message) {
        // Validate that emetteur and recepteur exist
        Optional<User> emetteurOpt = userRepository.findById(message.getEmetteur().getId());
        Optional<User> recepteurOpt = userRepository.findById(message.getRecepteur().getId());
        
        if (emetteurOpt.isPresent() && recepteurOpt.isPresent()) {
            message.setEmetteur(emetteurOpt.get());
            message.setRecepteur(recepteurOpt.get());
            message.setDateTimeEnvoi(LocalDateTime.now());
            message.setCreatedAt(LocalDateTime.now());
            return messageRepository.save(message);
        }
        return null;
    }

    public Message createMessage(String contenu, Long emetteurId, Long recepteurId) {
        Optional<User> emetteurOpt = userRepository.findById(emetteurId);
        Optional<User> recepteurOpt = userRepository.findById(recepteurId);
        
        if (emetteurOpt.isPresent() && recepteurOpt.isPresent()) {
            Message message = new Message(
                contenu,
                LocalDateTime.now(),
                emetteurOpt.get(),
                recepteurOpt.get()
            );
            return messageRepository.save(message);
        }
        return null;
    }

    public boolean deleteMessage(Long id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deleteConversation(Long user1Id, Long user2Id) {
        List<Message> messages = messageRepository.findConversationBetweenUsers(user1Id, user2Id);
        messageRepository.deleteAll(messages);
        return true;
    }

    public List<Message> getRecentMessages(Long userId, int limit) {
        List<Message> conversations = messageRepository.findConversationsByUserId(userId);
        return conversations.stream()
            .limit(limit)
            .toList();
    }

    public long getUnreadMessageCount(Long userId) {
        // This would need a new repository method to count unread messages
        // For now, return total messages for the user
        return messageRepository.findByRecepteurId(userId).size();
    }
}
