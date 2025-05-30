package com.novademy.contracts.responses.packages;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PackageResponse(
    UUID id,
    @NotBlank String title,
    @NotBlank String description,
    @NotNull BigDecimal price,
    String imageUrl,
    Instant createdAt,
    Instant updatedAt,
    @NotEmpty List<UUID> courseIds
) {} 