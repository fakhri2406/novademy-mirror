package com.novademy.contracts.requests.auth;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record VerifyEmailRequest(
    @NotNull UUID userId,
    @NotBlank String code
) {} 