package com.novademy.application.external.token;

import com.novademy.application.entities.User;

public interface TokenGenerator {
    String generateAccessToken(User user);
    String generateRefreshToken();
} 