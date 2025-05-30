package com.novademy.contracts.requests.lesson;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

import java.util.UUID;

public record CreateLessonRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull MultipartFile video,
    @Min(0) int order,
    boolean isFree,
    @NotBlank String transcript,
    MultipartFile image,
    @NotNull UUID courseId
) {} 