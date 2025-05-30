package com.novademy.contracts.responses.auth;

import jakarta.validation.constraints.*;

public record AuthResponse(
    @NotBlank String accessToken,
    @NotBlank String refreshToken
) {} 