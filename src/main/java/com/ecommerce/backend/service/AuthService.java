package com.ecommerce.backend.service;

import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.exceptions.OtpException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.exceptions.UserAlreadyExistsException;
import com.ecommerce.backend.exceptions.UserException;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.request.SignupRequest;

import jakarta.mail.MessagingException;

public interface AuthService {

        // SIGNUP
        String createUser(SignupRequest request) throws UserException, OtpException, UserAlreadyExistsException;

        // SIGNUP OTP
        void sendSignupOtp(String email)
                        throws MessagingException, UserException, UserAlreadyExistsException;

        // LOGIN OTP
        void sendLoginOtp(
                        String email,
                        USER_ROLE role)
                        throws MessagingException, UserException, SellerException;

        // LOGIN
        AuthResponse signing(LoginRequest request) throws OtpException;
}