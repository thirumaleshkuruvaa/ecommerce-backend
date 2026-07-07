package com.ecommerce.backend.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevProfile {

    public String message() {
        return "Running in DEV environment 🚀";
    }
}
