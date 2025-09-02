package com.elitecom.backend.config;

import com.elitecom.backend.entity.User;
import com.elitecom.backend.entity.Role;
import com.elitecom.backend.entity.ValidationStatus;
import com.elitecom.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            // Check if data already exists
            if (userRepository.count() > 0) {
                System.out.println("Database already populated, skipping initialization");
                return;
            }

            System.out.println("Initializing database with sample data...");

            // Create Admin User
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("EliteCom");
            admin.setEmail("admin@elitecom.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            admin.setValidationStatus(ValidationStatus.APPROVED);
            userRepository.save(admin);

            // Create Sample Client
            User client = new User();
            client.setFirstName("John");
            client.setLastName("Doe");
            client.setEmail("john.doe@example.com");
            client.setPassword(passwordEncoder.encode("client123"));
            client.setRole(Role.CLIENT);
            client.setActive(true);
            client.setValidationStatus(ValidationStatus.APPROVED);
            userRepository.save(client);

            // Create Sample Practitioners for each domain
            String[] domains = {"Health", "Law", "Aesthetics", "Well-being", "Education", "Decoration"};
            String[] emails = {
                "sarah.johnson@elitecom.com",
                "lawyer.smith@elitecom.com", 
                "beauty.expert@elitecom.com",
                "wellness.coach@elitecom.com",
                "education.tutor@elitecom.com",
                "interior.designer@elitecom.com"
            };

            for (int i = 0; i < domains.length; i++) {
                User practitioner = new User();
                practitioner.setFirstName(domains[i]);
                practitioner.setLastName("Practitioner");
                practitioner.setEmail(emails[i]);
                practitioner.setPassword(passwordEncoder.encode("practitioner123"));
                practitioner.setRole(Role.PRACTITIONER);
                practitioner.setActive(true);
                practitioner.setValidationStatus(ValidationStatus.APPROVED); // Pre-approved for sample data
                userRepository.save(practitioner);
            }

            System.out.println("Database initialization completed!");
            System.out.println("Created " + userRepository.count() + " users");
            
        } catch (Exception e) {
            System.err.println("Error during database initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
