package com.ecommerce.backend.service.Impl;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        private final JwtProvider jwtProvider;

        public UserServiceImpl(
                        UserRepository userRepository,
                        JwtProvider jwtProvider) {

                this.userRepository = userRepository;
                this.jwtProvider = jwtProvider;
        }

        // FIND USER BY JWT TOKEN

        @Override
        public User findUserByJwtToken(String jwt) {

                log.info("Finding user using JWT token");

                // REMOVE "Bearer "

                if (jwt.startsWith("Bearer ")) {

                        log.debug("Removing Bearer prefix from JWT token");

                        jwt = jwt.substring(7);
                }

                String email = jwtProvider
                                .getEmailFromJwtToken(jwt);

                log.debug("Extracted email from JWT : {}",
                                email);

                User user = this.findUserByEmail(email);

                log.info("User fetched successfully using JWT");

                return user;
        }

        // FIND USER BY EMAIL

        @Override
        public User findUserByEmail(String email) {

                log.info("Finding user by email : {}",
                                email);

                return userRepository.findByEmail(email)

                                .orElseThrow(() -> {

                                        log.error("User not found with email : {}",
                                                        email);

                                        return new RuntimeException(
                                                        "User not found with email - "
                                                                        + email);
                                });
        }
}