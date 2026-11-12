package com.stockflow.stockflow_backend.Service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stockflow.stockflow_backend.Repository.OrganizationRepository;
import com.stockflow.stockflow_backend.Repository.UserRepository;
import com.stockflow.stockflow_backend.Service.AuthService;
import com.stockflow.stockflow_backend.dto.AuthResponse;
import com.stockflow.stockflow_backend.dto.LoginRequest;
import com.stockflow.stockflow_backend.dto.RegisterRequest;
import com.stockflow.stockflow_backend.entity.AppUser;
import com.stockflow.stockflow_backend.entity.Organization;
import com.stockflow.stockflow_backend.security.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            OrganizationRepository organizationRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // USER REGISTRATION
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // Check whether email already exists
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create organization
        Organization organization = new Organization();
        organization.setName(request.getOrganizationName().trim());
        organization = organizationRepository.save(organization);

        // Create user
        AppUser user = new AppUser();

        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim());
        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );
        user.setOrganization(organization);

        user = userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(
            user.getEmail(),
            organization.getId()
        );

        return new AuthResponse(
            token,
            user.getName(),
            user.getEmail(),
            organization.getId()
        );
    }

    // USER LOGIN
    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        // Find user using email
        AppUser user = userRepository
                .findByEmail(request.getEmail().trim())
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid email or password")
                );

        // Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new IllegalArgumentException(
                "Invalid email or password"
            );
        }

        // Generate JWT token
        String token = jwtService.generateToken(
            user.getEmail(),
            user.getOrganization().getId()
        );

        return new AuthResponse(
            token,
            user.getName(),
            user.getEmail(),
            user.getOrganization().getId()
        );
    }
}