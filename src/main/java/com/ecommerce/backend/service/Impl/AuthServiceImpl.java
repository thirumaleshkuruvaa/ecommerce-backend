package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.backend.config.JwtProvider;

import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.exceptions.*;
import com.ecommerce.backend.model.*;
import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.request.SignupRequest;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.AuthService;
import com.ecommerce.backend.service.EmailService;
import com.ecommerce.backend.util.OtpUtil;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

        private final UserRepository userRepository;
        private final SellerRepository sellerRepository;
        private final CartRepository cartRepository;
        private final VerificationCodeRepository verificationCodeRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtProvider jwtProvider;
        private final EmailService emailService;

        public AuthServiceImpl(
                        UserRepository userRepository,
                        SellerRepository sellerRepository,
                        CartRepository cartRepository,
                        VerificationCodeRepository verificationCodeRepository,
                        PasswordEncoder passwordEncoder,
                        JwtProvider jwtProvider,
                        EmailService emailService) {

                this.userRepository = userRepository;
                this.sellerRepository = sellerRepository;
                this.cartRepository = cartRepository;
                this.verificationCodeRepository = verificationCodeRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtProvider = jwtProvider;
                this.emailService = emailService;
        }

        // SIGNUP USER

        @Override
        public String createUser(SignupRequest request)
                        throws UserException, OtpException, UserAlreadyExistsException {

                log.info("SIGNUP STARTED for email: {}", request.getEmail());

                VerificationCode code = verificationCodeRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> {
                                        log.error("OTP NOT FOUND for email: {}", request.getEmail());
                                        return new RuntimeException("OTP not found");
                                });

                log.info("OTP FOUND for email: {}", request.getEmail());

                if (!code.getOtp().equals(request.getOtp())) {
                        log.error("INVALID OTP for email: {}", request.getEmail());
                        throw new OtpException("Wrong OTP");
                }

                log.info("OTP VERIFIED successfully for email: {}", request.getEmail());

                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                        log.error("USER ALREADY EXISTS: {}", request.getEmail());
                        throw new UserAlreadyExistsException("User already exists");
                }

                log.info("CREATING USER...");

                User user = new User();
                user.setEmail(request.getEmail());
                user.setFullName(request.getFullName());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setPhoneNumber(request.getPhoneNumber());
                user.setRole(USER_ROLE.ROLE_CUSTOMER);

                userRepository.save(user);

                log.info("USER SAVED with ID: {}", user.getId());

                Cart cart = new Cart();
                cart.setUser(user);
                cartRepository.save(cart);

                log.info("CART CREATED for USER ID: {}", user.getId());

                verificationCodeRepository.delete(code);
                log.info("OTP DELETED for email: {}", request.getEmail());

                Authentication auth = new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                null,
                                List.of(new SimpleGrantedAuthority(user.getRole().name())));

                SecurityContextHolder.getContext().setAuthentication(auth);

                String token = jwtProvider.generateToken(auth);

                log.info("JWT GENERATED SUCCESSFULLY for USER: {}", user.getEmail());

                return token;
        }

        // SEND SIGNUP OTP

        @Transactional
        @Override
        public void sendSignupOtp(String email) throws MessagingException {

                log.info("SIGNUP OTP REQUEST for email: {}", email);

                verificationCodeRepository.deleteByEmail(email);
                log.info("OLD SIGNUP OTP DELETED for email: {}", email);

                String otp = OtpUtil.generateOtp();
                log.info("OTP GENERATED: {}", otp);

                VerificationCode code = new VerificationCode();
                code.setEmail(email);
                code.setOtp(otp);
                code.setType("SIGNUP");

                verificationCodeRepository.save(code);

                log.info("OTP SAVED for email: {}", email);

                emailService.sendVerificationOtpEmail(
                                email,
                                otp,
                                "Signup OTP",
                                "Your OTP is " + otp);

                log.info("SIGNUP OTP EMAIL SENT to: {}", email);
        }

        // SEND LOGIN OTP

        @Transactional
        @Override
        public void sendLoginOtp(String email, USER_ROLE role) throws MessagingException {

                log.info("LOGIN OTP REQUEST for email: {}", email);

                verificationCodeRepository.deleteByEmail(email);
                log.info("OLD LOGIN OTP DELETED for email: {}", email);

                String otp = OtpUtil.generateOtp();
                log.info("LOGIN OTP GENERATED: {}", otp);

                VerificationCode code = new VerificationCode();
                code.setEmail(email);
                code.setOtp(otp);
                code.setType("LOGIN");

                verificationCodeRepository.save(code);

                log.info("LOGIN OTP SAVED for email: {}", email);

                emailService.sendVerificationOtpEmail(
                                email,
                                otp,
                                "Login OTP",
                                "Your login OTP is " + otp);

                log.info("LOGIN OTP EMAIL SENT to: {}", email);
        }

        // LOGIN VERIFY

        @Override
        public AuthResponse signing(LoginRequest request) {

                log.info("LOGIN STARTED for email: {}", request.getEmail());

                VerificationCode code = verificationCodeRepository.findByEmail(request.getEmail())
                                .orElse(null);

                if (code == null || !code.getOtp().equals(request.getOtp())) {
                        log.error("INVALID OTP for email: {}", request.getEmail());
                        AuthResponse res = new AuthResponse();
                        res.setMessege("Invalid OTP");
                        return res;
                }

                verificationCodeRepository.delete(code);

                USER_ROLE role;

                // 1) CHECK USER TABLE FIRST (ADMIN / CUSTOMER)
                User user = userRepository.findByEmail(request.getEmail()).orElse(null);

                if (user != null) {
                        role = user.getRole();
                        log.info("ROLE from user table = {}", role);
                }

                // 2) CHECK SELLER TABLE
                else if (sellerRepository.findByEmail(request.getEmail()).isPresent()) {
                        role = USER_ROLE.ROLE_SELLER;
                        log.info("ROLE = SELLER");
                }

                // 3) NO USER FOUND
                else {
                        log.error("USER NOT FOUND");
                        throw new RuntimeException("User not found");
                }
                Authentication auth = new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                null,
                                List.of(new SimpleGrantedAuthority(role.name())));

                SecurityContextHolder.getContext().setAuthentication(auth);

                String token = jwtProvider.generateToken(auth);

                log.info("JWT GENERATED with ROLE: {}", role);

                AuthResponse response = new AuthResponse();
                response.setJwt(token);
                response.setMessege("Login Success");
                response.setRole(role);

                return response;
        }

}