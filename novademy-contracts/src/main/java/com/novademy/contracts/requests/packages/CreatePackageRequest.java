package com.novademy.contracts.requests.packages;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreatePackageRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull BigDecimal price,
    MultipartFile image,
    @NotEmpty List<@NotNull UUID> courseIds
) {} 