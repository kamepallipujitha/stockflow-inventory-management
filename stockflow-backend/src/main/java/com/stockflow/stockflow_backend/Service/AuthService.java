package com.stockflow.stockflow_backend.Service;

import com.stockflow.stockflow_backend.dto.AuthResponse;
import com.stockflow.stockflow_backend.dto.LoginRequest;
import com.stockflow.stockflow_backend.dto.RegisterRequest;

public interface AuthService {
	AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
