package com.stockflow.stockflow_backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stockflow.stockflow_backend.Service.AuthService;
import com.stockflow.stockflow_backend.dto.AuthResponse;
import com.stockflow.stockflow_backend.dto.LoginRequest;
import com.stockflow.stockflow_backend.dto.RegisterRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	 private final AuthService authService;

	    public AuthController(AuthService authService) {
	        this.authService = authService;
	    }

	    // Registration API
	    @PostMapping("/register")
	    public ResponseEntity<AuthResponse> register(
	            @Valid @RequestBody RegisterRequest request) {

	        AuthResponse response = authService.register(request);

	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }

	    // Login API
	    @PostMapping("/login")
	    public ResponseEntity<AuthResponse> login(
	            @Valid @RequestBody LoginRequest request) {

	        AuthResponse response = authService.login(request);

	        return ResponseEntity.ok(response);
	    }
}
