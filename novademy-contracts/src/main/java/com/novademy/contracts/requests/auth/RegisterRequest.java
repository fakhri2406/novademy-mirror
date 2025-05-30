package com.novademy.contracts.requests.auth;

import com.novademy.contracts.enums.SectorType;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.*;

public record RegisterRequest(
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email @NotBlank String email,
    @NotBlank String phoneNumber,
    @Min(1) int roleId,
    int group,
    @NotNull SectorType sector,
    MultipartFile profilePicture
) {} 