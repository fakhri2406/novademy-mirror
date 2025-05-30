package com.novademy.contracts.requests.lesson;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

public record UpdateLessonRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull MultipartFile video,
    @Min(0) int order,
    @NotBlank String transcript,
    MultipartFile image
) {} 