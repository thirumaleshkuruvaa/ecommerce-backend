package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.Impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setEmail("thirumalesh@gmail.com");
        user.setFullName("Thirumalesh");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
    }

    // -------------------------------------------------------
    // FIND USER BY JWT TOKEN (Bearer Token)
    // -------------------------------------------------------

    @Test
    void testFindUserByJwtToken_WithBearerPrefix() {

        String jwt = "Bearer test_token";

        when(jwtProvider.getEmailFromJwtToken("test_token"))
                .thenReturn("thirumalesh@gmail.com");

        when(userRepository.findByEmail("thirumalesh@gmail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findUserByJwtToken(jwt);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getId(), result.getId());

        verify(jwtProvider).getEmailFromJwtToken("test_token");
        verify(userRepository).findByEmail("thirumalesh@gmail.com");
    }

    // -------------------------------------------------------
    // FIND USER BY JWT TOKEN (Without Bearer)
    // -------------------------------------------------------

    @Test
    void testFindUserByJwtToken_WithoutBearerPrefix() {

        String jwt = "plain_token";

        when(jwtProvider.getEmailFromJwtToken("plain_token"))
                .thenReturn("thirumalesh@gmail.com");

        when(userRepository.findByEmail("thirumalesh@gmail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findUserByJwtToken(jwt);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());

        verify(jwtProvider).getEmailFromJwtToken("plain_token");
        verify(userRepository).findByEmail("thirumalesh@gmail.com");
    }

    // -------------------------------------------------------
    // FIND USER BY EMAIL SUCCESS
    // -------------------------------------------------------

    @Test
    void testFindUserByEmail_Success() {

        when(userRepository.findByEmail("thirumalesh@gmail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.findUserByEmail("thirumalesh@gmail.com");

        assertNotNull(result);
        assertEquals("thirumalesh@gmail.com", result.getEmail());
        assertEquals(USER_ROLE.ROLE_CUSTOMER, result.getRole());

        verify(userRepository).findByEmail("thirumalesh@gmail.com");
    }
}