package com.ecommerce.backend.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                log.info("Initializing Spring Security configuration");

                http
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .csrf(csrf -> csrf.disable())

                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                .authorizeHttpRequests(auth -> auth

                                                // Preflight requests
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                // PUBLIC
                                                .requestMatchers(
                                                                "/api/auth/**",
                                                                "/products/**",
                                                                "/sellers/**",
                                                                "/api/deals/**",
                                                                "/api/reviews/products/**",

                                                                "/api/home",
                                                                "/api/home/**",
                                                                "/api/chat/**",

                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui.html",
                                                                "/actuator/**")
                                                .permitAll()

                                                // CUSTOMER
                                                .requestMatchers(
                                                                "/api/users/**",
                                                                "/api/cart/**",
                                                                "/api/orders/**",
                                                                "/api/wishlist/**",
                                                                "/api/reviews/**",
                                                                "/api/addresses/**",
                                                                "/api/coupons/apply",
                                                                "/api/payment/**")
                                                .hasAuthority("ROLE_CUSTOMER")

                                                // SELLER
                                                .requestMatchers(
                                                                "/api/sellers/products/**",
                                                                "/api/sellers/orders/**",
                                                                "/api/transactions/seller",
                                                                "/sellers/profile")
                                                .hasAuthority("ROLE_SELLER")

                                                // ADMIN
                                                .requestMatchers(
                                                                "/api/admin/**",
                                                                "/api/transactions/**",
                                                                "/api/coupons/admin/**")
                                                .hasAuthority("ROLE_ADMIN")

                                                .anyRequest().authenticated())

                                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class);

                log.info("Security configuration loaded successfully");

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

                log.info("Configuring CORS");

                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOriginPatterns(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setExposedHeaders(List.of("Authorization"));
                config.setAllowCredentials(false);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);

                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}