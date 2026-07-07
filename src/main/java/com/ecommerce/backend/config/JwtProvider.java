package com.ecommerce.backend.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtProvider {

        private static final SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());

        public String generateToken(Authentication auth) {

                String roles = String.join(",",
                                auth.getAuthorities()
                                                .stream()
                                                .map(GrantedAuthority::getAuthority)
                                                .toList());

                return Jwts.builder()
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                                .claim("email", auth.getName())
                                .claim("authorities", roles)
                                .signWith(key)
                                .compact();
        }

        public String getEmailFromJwtToken(String jwt) {

                log.info("Extracting email from JWT token");

                Claims claims = getClaimsFromToken(jwt);

                String email = String.valueOf(claims.get("email"));

                log.info("Email extracted from token : {}", email);

                return email;
        }

        public static Claims getClaimsFromToken(String jwt) {

                log.info("Extracting claims from JWT token");

                Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(jwt)
                                .getBody();

                log.info("JWT claims extracted successfully");

                return claims;
        }

        private String populateAuthorities(
                        Collection<? extends GrantedAuthority> authorities) {

                log.info("Populating user authorities");

                Set<String> auths = new HashSet<>();

                for (GrantedAuthority authority : authorities) {

                        log.debug("Authority found : {}",
                                        authority.getAuthority());

                        auths.add(authority.getAuthority());
                }

                String roles = String.join(",", auths);

                log.info("Authorities populated : {}", roles);

                return roles;
        }
}