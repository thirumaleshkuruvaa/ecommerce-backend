package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.exceptions.OtpException;
import com.ecommerce.backend.exceptions.UserAlreadyExistsException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.model.VerificationCode;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.repository.VerificationCodeRepository;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.request.SignupRequest;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.EmailService;
import com.ecommerce.backend.service.Impl.AuthServiceImpl;

import jakarta.mail.MessagingException;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

        @Mock
        private UserRepository userRepository;

        @Mock
        private SellerRepository sellerRepository;

        @Mock
        private CartRepository cartRepository;

        @Mock
        private VerificationCodeRepository verificationCodeRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtProvider jwtProvider;

        @Mock
        private EmailService emailService;

        @InjectMocks
        private AuthServiceImpl authService;

        private SignupRequest signupRequest;
        private LoginRequest loginRequest;
        private VerificationCode verificationCode;
        private User user;

        @BeforeEach
        void setUp() {

                signupRequest = new SignupRequest();
                signupRequest.setEmail("test@gmail.com");
                signupRequest.setPassword("123456");
                signupRequest.setFullName("John");
                signupRequest.setPhoneNumber("9876543210");
                signupRequest.setOtp("123456");

                loginRequest = new LoginRequest();
                loginRequest.setEmail("test@gmail.com");
                loginRequest.setOtp("123456");

                verificationCode = new VerificationCode();
                verificationCode.setEmail("test@gmail.com");
                verificationCode.setOtp("123456");

                user = new User();
                user.setId(1L);
                user.setEmail("test@gmail.com");
                user.setRole(USER_ROLE.ROLE_CUSTOMER);
        }

        @Test
        void createUserSuccess() throws Exception {

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                when(userRepository.findByEmail(anyString()))
                                .thenReturn(Optional.empty());

                when(passwordEncoder.encode(anyString()))
                                .thenReturn("encodedPassword");

                when(jwtProvider.generateToken(any(UsernamePasswordAuthenticationToken.class)))
                                .thenReturn("jwt-token");

                String token = authService.createUser(signupRequest);

                assertEquals("jwt-token", token);

                verify(userRepository).save(any(User.class));
                verify(cartRepository).save(any(Cart.class));
                verify(verificationCodeRepository).delete(any(VerificationCode.class));
        }

        @Test
        void createUserWrongOtp() {

                verificationCode.setOtp("999999");

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                assertThrows(OtpException.class,
                                () -> authService.createUser(signupRequest));

                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void createUserAlreadyExists() {

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                when(userRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(user));

                assertThrows(UserAlreadyExistsException.class,
                                () -> authService.createUser(signupRequest));

                verify(userRepository, never()).save(any(User.class));
        }

        @Test
        void sendSignupOtpSuccess() throws MessagingException {

                doNothing().when(verificationCodeRepository).deleteByEmail(anyString());

                when(verificationCodeRepository.save(any(VerificationCode.class)))
                                .thenReturn(new VerificationCode());

                doNothing().when(emailService)
                                .sendVerificationOtpEmail(
                                                anyString(),
                                                anyString(),
                                                anyString(),
                                                anyString());

                authService.sendSignupOtp("test@gmail.com");

                verify(verificationCodeRepository).deleteByEmail("test@gmail.com");
                verify(verificationCodeRepository).save(any(VerificationCode.class));

                verify(emailService).sendVerificationOtpEmail(
                                eq("test@gmail.com"),
                                anyString(),
                                eq("Signup OTP"),
                                anyString());
        }

        @Test
        void sendLoginOtpSuccess() throws MessagingException {

                doNothing().when(verificationCodeRepository).deleteByEmail(anyString());

                when(verificationCodeRepository.save(any(VerificationCode.class)))
                                .thenReturn(new VerificationCode());

                doNothing().when(emailService)
                                .sendVerificationOtpEmail(
                                                anyString(),
                                                anyString(),
                                                anyString(),
                                                anyString());

                authService.sendLoginOtp(
                                "test@gmail.com",
                                USER_ROLE.ROLE_CUSTOMER);

                verify(verificationCodeRepository).deleteByEmail("test@gmail.com");
                verify(verificationCodeRepository).save(any(VerificationCode.class));

                verify(emailService).sendVerificationOtpEmail(
                                eq("test@gmail.com"),
                                anyString(),
                                eq("Login OTP"),
                                anyString());
        }

        @Test
        void loginSuccessCustomer() {

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                when(userRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(user));

                when(jwtProvider.generateToken(any()))
                                .thenReturn("jwt-token");

                AuthResponse response = authService.signing(loginRequest);

                assertEquals("jwt-token", response.getJwt());
                assertEquals("Login Success", response.getMessege());
                assertEquals(USER_ROLE.ROLE_CUSTOMER, response.getRole());

                verify(verificationCodeRepository).delete(any(VerificationCode.class));
        }

        @Test
        void loginInvalidOtp() {

                verificationCode.setOtp("999999");

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                AuthResponse response = authService.signing(loginRequest);

                assertEquals("Invalid OTP", response.getMessege());
                assertNull(response.getJwt());

                verify(jwtProvider, never()).generateToken(any());
        }

        @Test
        void loginSellerSuccess() {

                Seller seller = new Seller();
                seller.setEmail("test@gmail.com");

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                when(userRepository.findByEmail(anyString()))
                                .thenReturn(Optional.empty());

                when(sellerRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(seller));

                when(jwtProvider.generateToken(any()))
                                .thenReturn("seller-token");

                AuthResponse response = authService.signing(loginRequest);

                assertEquals(USER_ROLE.ROLE_SELLER, response.getRole());
                assertEquals("seller-token", response.getJwt());
                assertEquals("Login Success", response.getMessege());
        }

        @Test
        void loginUserNotFound() {

                when(verificationCodeRepository.findByEmail(anyString()))
                                .thenReturn(Optional.of(verificationCode));

                when(userRepository.findByEmail(anyString()))
                                .thenReturn(Optional.empty());

                when(sellerRepository.findByEmail(anyString()))
                                .thenReturn(Optional.empty());

                assertThrows(RuntimeException.class,
                                () -> authService.signing(loginRequest));
        }
}