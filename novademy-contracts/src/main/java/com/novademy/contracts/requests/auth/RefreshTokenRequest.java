package com.novademy.contracts.requests.auth;

import jakarta.validation.constraints.*;

public record RefreshTokenRequest(
    @NotBlank String token
) {} 