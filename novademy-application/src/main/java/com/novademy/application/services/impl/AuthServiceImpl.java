package com.novademy.application.services.impl;

import com.novademy.application.config.JwtProperties;
import com.novademy.application.entities.RefreshToken;
import com.novademy.application.entities.User;
import com.novademy.application.external.cloudinary.CloudinaryService;
import com.novademy.application.external.email.IEmailService;
import com.novademy.application.external.token.TokenGenerator;
import com.novademy.application.helpers.PasswordHasher;
import com.novademy.application.mapping.ContractMapping;
import com.novademy.application.repositories.RefreshTokenRepository;
import com.novademy.application.repositories.RoleRepository;
import com.novademy.application.repositories.UserRepository;
import com.novademy.application.services.AuthService;
import com.novademy.contracts.requests.auth.LoginRequest;
import com.novademy.contracts.requests.auth.RefreshTokenRequest;
import com.novademy.contracts.requests.auth.RegisterRequest;
import com.novademy.contracts.requests.auth.VerifyEmailRequest;
import com.novademy.contracts.responses.auth.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CloudinaryService cloudinaryService;
    private final IEmailService emailService;
    private final TokenGenerator tokenGenerator;
    private final JwtProperties jwtProperties;

    @Override
    public UUID register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }
        if (userRepository.findByPhoneNumber(request.phoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists.");
        }

        User user = ContractMapping.toUser(request);
        String hashed = PasswordHasher.hash(request.password() + user.getSalt());
        user.setPassword(hashed);

        try {
            if (request.profilePicture() != null) {
                var uploadResult = cloudinaryService.uploadImage(request.profilePicture(), "users");
                user.setProfilePictureUrl((String) uploadResult.get("secure_url"));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to upload profile picture.", ex);
        }

        String code = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
        user.setEmailVerificationCode(code);
        user.setEmailVerificationExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        try {
            emailService.sendEmail(user.getEmail(), "Verify your email", "Your verification code is: " + code, false);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send verification email.", ex);
        }

        return user.getId();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new NoSuchElementException("Invalid username or password."));

        String hashed = PasswordHasher.hash(request.password() + user.getSalt());
        if (!hashed.equals(user.getPassword())) {
            throw new NoSuchElementException("Invalid username or password.");
        }

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String accessToken = tokenGenerator.generateAccessToken(user);
        String refreshTokenValue = tokenGenerator.generateRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
            .id(UUID.randomUUID())
            .token(refreshTokenValue)
            .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenValiditySeconds()))
            .userId(user.getId())
            .build();
        refreshTokenRepository.save(refreshToken);

        return ContractMapping.toAuthResponse(accessToken, refreshTokenValue);
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new NoSuchElementException("User not found."));

        if (user.getEmailVerificationCode() == null || !user.getEmailVerificationCode().equals(request.code())) {
            throw new IllegalArgumentException("Invalid verification code.");
        }
        if (user.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code expired.");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        user.setEmailVerificationExpiry(null);
        userRepository.save(user);
    }

    @Override
    public AuthResponse refresh(RefreshTokenRequest request) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(request.token())
            .orElseThrow(() -> new NoSuchElementException("Invalid refresh token."));

        if (oldToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(oldToken);
            throw new IllegalArgumentException("Refresh token expired.");
        }

        User user = oldToken.getUser();
        refreshTokenRepository.delete(oldToken);

        String newRefresh = tokenGenerator.generateRefreshToken();
        RefreshToken newToken = RefreshToken.builder()
            .id(UUID.randomUUID())
            .token(newRefresh)
            .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenValiditySeconds()))
            .userId(user.getId())
            .build();
        refreshTokenRepository.save(newToken);

        String access = tokenGenerator.generateAccessToken(user);
        return ContractMapping.toAuthResponse(access, newRefresh);
    }

    @Override
    public void logout(UUID userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findByUserId(userId);
        if (tokens.isEmpty()) {
            throw new NoSuchElementException("No refresh tokens found for the user.");
        }
        refreshTokenRepository.deleteAll(tokens);
    }
} 