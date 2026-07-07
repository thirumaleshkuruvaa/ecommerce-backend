package com.ecommerce.backend.domain;

public enum AccountStatus {
    PENDING_VERIFICATION, // Account is created but not yet verified
    ACTIVE, // Account is active and can perform all operations
    SUSPENDED, // Account is suspended ,possibly due to policy violations or security concerns
    DEACTIVATED, // Account is deactivated by the user
    BANNED, // Account is banned due to server violations
    CLOSED // Account is closed ,possibly due to user request or inactivity
}
