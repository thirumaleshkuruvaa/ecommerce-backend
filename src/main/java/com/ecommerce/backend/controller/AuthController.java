package com.ecommerce.backend.controller;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.domain.AdminLoginRequest;
import com.ecommerce.backend.domain.CreateAdminRequest;
import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.exceptions.OtpException;
import com.ecommerce.backend.exceptions.UserAlreadyExistsException;
import com.ecommerce.backend.exceptions.UserException;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.request.LoginOtpRequest;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.request.SignupOtpRequest;
import com.ecommerce.backend.request.SignupRequest;

import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Authentication", description = "User Registration, Login, OTP Verification and Admin Management APIs")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthController(
            AuthService authService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,

            JwtProvider jwtProvider) {

        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    // Send Signup OTP

    @Operation(summary = "Send Signup OTP", description = "Send OTP to user's email for account registration")
    @PostMapping("/send/signup-otp")
    public ResponseEntity<ApiResponse> sendSignupOtp(
            @Valid @RequestBody SignupOtpRequest req)
            throws Exception {

        authService.sendSignupOtp(req.getEmail());

        ApiResponse response = new ApiResponse();
        response.setMessege("Signup OTP sent successfully");

        return ResponseEntity.ok(response);
    }

    // Register New User

    @Operation(summary = "Register New User", description = "Create a new customer account using OTP verification")
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignupRequest request)
            throws UserException, OtpException, UserAlreadyExistsException {

        log.info("Signup request for email: {}", request.getEmail());

        String jwt = authService.createUser(request);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessege("Registration successful");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    // Send Login OTP

    @Operation(summary = "Send Login OTP", description = "Send OTP to user's email for login")
    @PostMapping("/send/login-otp")
    public ResponseEntity<ApiResponse> sendLoginOtp(
            @Valid @RequestBody LoginOtpRequest req)
            throws Exception {

        log.info("Login OTP request email: {}, role: {}",
                req.getEmail(),
                req.getRole());

        authService.sendLoginOtp(
                req.getEmail(),
                req.getRole());

        ApiResponse response = new ApiResponse();
        response.setMessege("Login OTP sent successfully");

        return ResponseEntity.ok(response);
    }

    // Login User

    @Operation(summary = "Login User", description = "Verify OTP and generate JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request)
            throws Exception {

        log.info("Login request for email: {}", request.getEmail());

        AuthResponse response = authService.signing(request);

        return ResponseEntity.ok(response);
    }

    // Create Admin Account

    @Operation(summary = "Create Admin Account", description = "Create a new administrator account")
    @PostMapping("/create-admin")
    public String createAdmin(
            @RequestBody CreateAdminRequest request) {

        User admin = new User();

        admin.setEmail(request.getEmail());

        admin.setPassword(
                passwordEncoder.encode(request.getPassword()));

        admin.setRole(USER_ROLE.ROLE_ADMIN);

        userRepository.save(admin);

        return "Admin created successfully";
    }

    // admin login
    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(
            @RequestBody AdminLoginRequest request)
            throws Exception {

        User admin = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (admin.getRole() != USER_ROLE.ROLE_ADMIN) {
            throw new RuntimeException("Access denied");
        }

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                admin.getEmail(),
                null,
                List.of(new SimpleGrantedAuthority(admin.getRole().name())));

        String token = jwtProvider.generateToken(authentication);

        AuthResponse response = new AuthResponse();
        response.setJwt(token);
        response.setRole(USER_ROLE.ROLE_ADMIN);
        response.setMessege("Admin Login Success");

        return ResponseEntity.ok(response);
    }
}