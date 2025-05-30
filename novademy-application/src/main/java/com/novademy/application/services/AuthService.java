package com.novademy.application.services;

import com.novademy.contracts.requests.auth.LoginRequest;
import com.novademy.contracts.requests.auth.RefreshTokenRequest;
import com.novademy.contracts.requests.auth.RegisterRequest;
import com.novademy.contracts.requests.auth.VerifyEmailRequest;
import com.novademy.contracts.responses.auth.AuthResponse;

import java.util.UUID;

public interface AuthService {
    UUID register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void verifyEmail(VerifyEmailRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
    void logout(UUID userId);
} 