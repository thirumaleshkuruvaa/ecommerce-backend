package com.ecommerce.backend.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserServiceImpl implements UserDetailsService {

        private final UserRepository userRepository;
        private final SellerRepository sellerRepository;

        private static final String SELLER_PREFIX = "seller_";

        public CustomUserServiceImpl(UserRepository userRepository,
                        SellerRepository sellerRepository) {
                this.userRepository = userRepository;
                this.sellerRepository = sellerRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) {

                log.info("Authenticating user {} ", username);

                if (username.startsWith(SELLER_PREFIX)) {
                        log.debug("Seller login  detected");

                        String email = username.substring(SELLER_PREFIX.length());

                        Seller seller = sellerRepository.findByEmail(email)

                                        .orElseThrow(() -> {

                                                log.error("Seller not found with email {}",
                                                                email);

                                                return new UsernameNotFoundException(
                                                                "Seller not found");
                                        });

                        log.info("Seller authenticated successfully with email {}",
                                        email);
                        return buildUser(seller.getEmail(), seller.getPassword(), seller.getRole());
                }

                User user = userRepository.findByEmail(username)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                return buildUser(user.getEmail(), user.getPassword(), user.getRole());
        }

        private UserDetails buildUser(String email, String password, USER_ROLE role) {

                log.debug("Building UserDetails object for role {}",
                                role);

                List<GrantedAuthority> auth = new ArrayList<>();
                auth.add(new SimpleGrantedAuthority(role.toString()));

                return new org.springframework.security.core.userdetails.User(
                                email, password, auth);
        }
}