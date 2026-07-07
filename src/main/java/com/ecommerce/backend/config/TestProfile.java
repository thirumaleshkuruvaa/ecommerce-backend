package com.ecommerce.backend.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class TestProfile {

    public String message() {
        return "Running in TEST environment 🚀";
    }

}
