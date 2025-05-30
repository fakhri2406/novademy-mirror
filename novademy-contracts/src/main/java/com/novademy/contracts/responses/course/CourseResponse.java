package com.novademy.contracts.responses.course;

import com.novademy.contracts.enums.SubjectType;
import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.UUID;

public record CourseResponse(
    UUID id,
    @NotBlank String title,
    @NotBlank String description,
    @NotNull SubjectType subject,
    String imageUrl,
    Instant createdAt,
    Instant updatedAt
) {} 