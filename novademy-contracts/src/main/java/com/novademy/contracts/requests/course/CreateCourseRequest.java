package com.novademy.contracts.requests.course;

import com.novademy.contracts.enums.SubjectType;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

public record CreateCourseRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull SubjectType subject,
    MultipartFile image
) {} 