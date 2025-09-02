package com.elitecom.backend.entity;

public enum ValidationStatus {
    PENDING,    // En attente de validation
    APPROVED,   // Validé par l'admin
    REJECTED,   // Rejeté par l'admin
    UNDER_REVIEW // En cours de révision
}
