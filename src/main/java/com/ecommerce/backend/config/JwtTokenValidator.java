package com.ecommerce.backend.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenValidator extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {

                String path = request.getServletPath();
                String method = request.getMethod();

                log.info("JWT FILTER HIT -> {} {}", method, path);

                // Skip preflight requests
                if (HttpMethod.OPTIONS.matches(method)) {
                        filterChain.doFilter(request, response);
                        return;
                }

                // Skip auth endpoints
                if (path.startsWith("/api/auth")) {
                        log.info("Skipping auth API");
                        filterChain.doFilter(request, response);
                        return;
                }

                String header = request.getHeader("Authorization");
                log.info("Authorization Header = {}", header);
                if (header == null || !header.startsWith("Bearer ")) {
                        log.warn("NO JWT TOKEN FOUND for {}", path);
                        filterChain.doFilter(request, response);
                        return;
                }

                try {
                        String jwt = header.substring(7);

                        Claims claims = JwtProvider.getClaimsFromToken(jwt);

                        String email = claims.get("email", String.class);
                        String authorities = claims.get("authorities", String.class);

                        log.info("EMAIL FROM TOKEN: {}", email);
                        log.info("AUTHORITIES FROM TOKEN: {}", authorities);

                        List<SimpleGrantedAuthority> auths = Arrays.stream(authorities.split(","))
                                        .map(String::trim)
                                        .filter(role -> !role.isEmpty())
                                        .map(SimpleGrantedAuthority::new)
                                        .toList();

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        email, null, auths);

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.info("SECURITY CONTEXT SET SUCCESSFULLY FOR {}", email);

                } catch (Exception e) {
                        log.error("JWT VALIDATION FAILED", e);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Invalid JWT");
                        return;
                }

                filterChain.doFilter(request, response);
        }
}