package com.novademy.contracts.requests.auth;

import jakarta.validation.constraints.*;

public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {} 