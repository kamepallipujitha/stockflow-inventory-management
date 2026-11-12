package com.stockflow.stockflow_backend.security;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stockflow.stockflow_backend.Repository.UserRepository;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.IOException;
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
	            throws ServletException, IOException, java.io.IOException {

	        // 1. Read Authorization header
	        String authHeader = request.getHeader("Authorization");

	        // 2. Check whether Bearer token exists
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            filterChain.doFilter(request, response);
	            return;
	        }

	        // 3. Extract token
	        String token = authHeader.substring(7);

	        try {
	            // 4. Extract email from token
	            String email = jwtService.extractEmail(token);

	            // 5. Check whether authentication already exists
	            if (email != null &&
	                    SecurityContextHolder.getContext().getAuthentication() == null) {

	                // 6. Check whether user exists
	                boolean userExists = userRepository.existsByEmail(email);

	                // 7. Validate token
	                if (userExists && jwtService.isTokenValid(token, email)) {

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
	                }
	            }

	        } catch (JwtException | IllegalArgumentException ex) {
	            SecurityContextHolder.clearContext();
	        }

	        // 9. Continue filter chain
	        filterChain.doFilter(request, response);
	    }
}
