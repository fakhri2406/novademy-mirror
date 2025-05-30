package com.novademy.contracts.requests.subscription;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record SubscriptionRequest(
    @NotNull UUID userId,
    @NotNull UUID packageId
) {} 