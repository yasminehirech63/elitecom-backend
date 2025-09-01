package com.elitecom.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ChatbotService {
    
    private Map<String, List<String>> domainKeywords;
    private Map<String, String> domainResponses;
    private List<String> predefinedQuestions;
    
    public ChatbotService() {
        initializeDomainKeywords();
        initializeDomainResponses();
        initializePredefinedQuestions();
    }
    
    public String processMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Bonjour! Je suis EliteCom, votre assistant virtuel. Comment puis-je vous aider aujourd'hui?";
        }
        
        String lowerMessage = message.toLowerCase().trim();
        
        // Check if it's a greeting
        if (isGreeting(lowerMessage)) {
            return getGreetingResponse();
        }
        
        // Check if it's a help request
        if (isHelpRequest(lowerMessage)) {
            return getHelpResponse();
        }
        
        // Check if question is in predefined list
        if (!isPredefinedQuestion(lowerMessage)) {
            return "Je suis désolé, mais je ne peux répondre qu'aux questions prédéfinies. Tapez 'aide' pour voir les domaines disponibles.";
        }
        
        // Detect domain and provide response
        String domain = detectDomain(lowerMessage);
        if (domain != null) {
            return domainResponses.get(domain);
        }
        
        return "Je peux vous aider dans les domaines suivants: Santé, Droit, Esthétique, Bien-être, Éducation, et Décoration. Quel domaine vous intéresse?";
    }
    
    private boolean isGreeting(String message) {
        String[] greetings = {"bonjour", "salut", "hello", "hi", "bonsoir", "hey"};
        return Arrays.stream(greetings).anyMatch(message::contains);
    }
    
    private boolean isHelpRequest(String message) {
        String[] helpKeywords = {"aide", "help", "assistance", "domaines", "services"};
        return Arrays.stream(helpKeywords).anyMatch(message::contains);
    }
    
    private String getGreetingResponse() {
        return "Bonjour! Je suis EliteCom, votre assistant virtuel spécialisé dans la mise en relation avec des professionnels qualifiés. " +
               "Je peux vous aider dans 6 domaines: Santé, Droit, Esthétique, Bien-être, Éducation, et Décoration. " +
               "Comment puis-je vous assister aujourd'hui?";
    }
    
    private String getHelpResponse() {
        return "🏥 **SANTÉ** - Cardiologie, Dermatologie, Pédiatrie, Psychiatrie\n" +
               "⚖️ **DROIT** - Droit civil, pénal, du travail, des affaires\n" +
               "💄 **ESTHÉTIQUE** - Soins du visage, épilation, massage, beauté\n" +
               "🧘 **BIEN-ÊTRE** - Coaching personnel/professionnel, nutrition, méditation\n" +
               "📚 **ÉDUCATION** - Cours particuliers, formation professionnelle, langues, préparation aux examens\n" +
               "🏠 **DÉCORATION** - Résidentielle, événementielle, commerciale, home staging\n\n" +
               "Quel domaine vous intéresse?";
    }
    
    private boolean isPredefinedQuestion(String message) {
        return predefinedQuestions.stream()
                .anyMatch(q -> message.contains(q.toLowerCase()));
    }
    
    private String detectDomain(String message) {
        for (Map.Entry<String, List<String>> entry : domainKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (message.contains(keyword.toLowerCase())) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    
    private void initializeDomainKeywords() {
        domainKeywords = new HashMap<>();
        
        domainKeywords.put("HEALTH", Arrays.asList(
            "médecin", "docteur", "santé", "maladie", "consultation", "cardiologie", 
            "dermatologie", "pédiatrie", "psychiatrie", "hôpital", "clinique"
        ));
        
        domainKeywords.put("LAW", Arrays.asList(
            "avocat", "droit", "juridique", "tribunal", "contrat", "divorce", 
            "pénal", "civil", "travail", "affaires", "justice"
        ));
        
        domainKeywords.put("AESTHETICS", Arrays.asList(
            "beauté", "esthétique", "massage", "soin", "visage", "épilation", 
            "cosmétique", "spa", "relaxation"
        ));
        
        domainKeywords.put("WELLBEING", Arrays.asList(
            "coaching", "bien-être", "nutrition", "méditation", "développement", 
            "personnel", "professionnel", "stress", "équilibre"
        ));
        
        domainKeywords.put("EDUCATION", Arrays.asList(
            "cours", "formation", "éducation", "apprentissage", "langue", 
            "examen", "particulier", "professionnelle", "école"
        ));
        
        domainKeywords.put("DECORATION", Arrays.asList(
            "décoration", "design", "intérieur", "maison", "événement", 
            "commercial", "staging", "aménagement"
        ));
    }
    
    private void initializeDomainResponses() {
        domainResponses = new HashMap<>();
        
        domainResponses.put("HEALTH", 
            "🏥 **DOMAINE SANTÉ**\n" +
            "Nos praticiens de santé qualifiés vous accompagnent dans:\n" +
            "• Cardiologie - Consultations cardiaques spécialisées\n" +
            "• Dermatologie - Soins de la peau et traitements\n" +
            "• Pédiatrie - Suivi médical pour enfants\n" +
            "• Psychiatrie - Accompagnement psychologique\n\n" +
            "Souhaitez-vous prendre rendez-vous avec un professionnel de santé?"
        );
        
        domainResponses.put("LAW", 
            "⚖️ **DOMAINE JURIDIQUE**\n" +
            "Nos avocats expérimentés vous conseillent en:\n" +
            "• Droit civil - Contrats, successions, famille\n" +
            "• Droit pénal - Défense et représentation\n" +
            "• Droit du travail - Relations employeur/employé\n" +
            "• Droit des affaires - Entreprises et commerces\n\n" +
            "Quel type de conseil juridique recherchez-vous?"
        );
        
        domainResponses.put("AESTHETICS", 
            "💄 **DOMAINE ESTHÉTIQUE**\n" +
            "Nos professionnels de beauté vous proposent:\n" +
            "• Soins du visage - Nettoyage, hydratation, anti-âge\n" +
            "• Épilation - Techniques modernes et efficaces\n" +
            "• Massage - Relaxation et bien-être\n" +
            "• Conseils beauté - Personnalisés selon vos besoins\n\n" +
            "Quel service esthétique vous intéresse?"
        );
        
        domainResponses.put("WELLBEING", 
            "🧘 **DOMAINE BIEN-ÊTRE**\n" +
            "Nos coachs certifiés vous accompagnent pour:\n" +
            "• Coaching personnel - Développement et épanouissement\n" +
            "• Coaching professionnel - Carrière et leadership\n" +
            "• Nutrition - Alimentation équilibrée et saine\n" +
            "• Méditation - Techniques de relaxation et mindfulness\n\n" +
            "Dans quel aspect souhaitez-vous être accompagné?"
        );
        
        domainResponses.put("EDUCATION", 
            "📚 **DOMAINE ÉDUCATION**\n" +
            "Nos formateurs qualifiés vous offrent:\n" +
            "• Cours particuliers - Soutien scolaire personnalisé\n" +
            "• Formation professionnelle - Développement de compétences\n" +
            "• Apprentissage des langues - Méthodes interactives\n" +
            "• Préparation aux examens - Stratégies de réussite\n\n" +
            "Quel type de formation recherchez-vous?"
        );
        
        domainResponses.put("DECORATION", 
            "🏠 **DOMAINE DÉCORATION**\n" +
            "Nos designers d'intérieur créent pour vous:\n" +
            "• Décoration résidentielle - Maisons et appartements\n" +
            "• Décoration événementielle - Mariages, fêtes, cérémonies\n" +
            "• Décoration commerciale - Bureaux, magasins, restaurants\n" +
            "• Home staging - Valorisation immobilière\n\n" +
            "Quel projet de décoration avez-vous en tête?"
        );
    }
    
    private void initializePredefinedQuestions() {
        predefinedQuestions = Arrays.asList(
            "j'ai besoin d'un médecin", "consultation médicale", "problème de santé",
            "besoin d'un avocat", "conseil juridique", "problème légal",
            "soin esthétique", "massage", "beauté",
            "coaching", "bien-être", "nutrition",
            "cours particuliers", "formation", "apprentissage",
            "décoration", "design intérieur", "aménagement",
            "aide", "domaines", "services disponibles"
        );
    }
    
    public List<String> getDomains() {
        return Arrays.asList("HEALTH", "LAW", "AESTHETICS", "WELLBEING", "EDUCATION", "DECORATION");
    }
}
