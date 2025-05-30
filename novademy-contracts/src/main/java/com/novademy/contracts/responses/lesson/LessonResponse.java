package com.novademy.contracts.responses.lesson;

import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.UUID;

public record LessonResponse(
    UUID id,
    @NotBlank String title,
    @NotBlank String description,
    @NotBlank String videoUrl,
    int order,
    boolean isFree,
    String transcript,
    String imageUrl,
    Instant createdAt,
    Instant updatedAt,
    @NotNull UUID courseId
) {} 