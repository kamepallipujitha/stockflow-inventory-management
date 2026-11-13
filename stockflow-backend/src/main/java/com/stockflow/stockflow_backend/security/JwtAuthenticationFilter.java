
package com.stockflow.stockflow_backend.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stockflow.stockflow_backend.Repository.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Read Authorization header
        String authHeader = request.getHeader("Authorization");

        System.out.println("\n========== JWT DEBUG ==========");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());

        // 2. Check whether Bearer token exists
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header missing or invalid.");
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract token
        String token = authHeader.substring(7);

        System.out.println("Authorization header received: true");
        System.out.println("Token length: " + token.length());

        try {
            // 4. Extract email from token
            String email = jwtService.extractEmail(token);

            System.out.println("JWT email extracted successfully: "
                    + (email != null));

            // 5. Check whether authentication already exists
            if (email != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. Check whether user exists
                boolean userExists = userRepository.existsByEmail(email);

                System.out.println("User exists in database: " + userExists);

                // 7. Validate token
                boolean valid = jwtService.isTokenValid(token, email);

                System.out.println("JWT validation successful: " + valid);

                if (userExists && valid) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 8. Store authentication
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "Spring Security authentication set successfully."
                    );

                } else {
                    System.out.println(
                            "Authentication failed: user missing or token invalid."
                    );
                }
            }

        } catch (JwtException | IllegalArgumentException ex) {

            System.out.println("JWT validation error: "
                    + ex.getClass().getSimpleName());

            SecurityContextHolder.clearContext();
        }

        // 9. Continue filter chain
        filterChain.doFilter(request, response);
    }
}
