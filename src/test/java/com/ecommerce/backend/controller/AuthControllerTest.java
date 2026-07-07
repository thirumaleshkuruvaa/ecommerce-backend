package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.domain.CreateAdminRequest;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.AuthService;

class AuthControllerTest {

    @Test
    void shouldCreateAdminAccount() {

        AuthService authService = mock(AuthService.class);

        UserRepository userRepository = mock(UserRepository.class);

        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        JwtProvider jwtProvider = mock(JwtProvider.class);

        AuthController controller = new AuthController(
                authService,
                userRepository,
                passwordEncoder,
                jwtProvider);

        CreateAdminRequest request = new CreateAdminRequest();

        request.setEmail("admin@example.com");
        request.setPassword("secret123");

        when(passwordEncoder.encode("secret123"))
                .thenReturn("encodedPassword");

        String result = controller.createAdmin(request);

        assertEquals("Admin created successfully", result);

        verify(userRepository).save(any(User.class));
    }
}