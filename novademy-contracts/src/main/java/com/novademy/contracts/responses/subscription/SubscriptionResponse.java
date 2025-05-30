package com.novademy.contracts.responses.subscription;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionResponse(
    UUID id,
    UUID userId,
    UUID packageId,
    Instant startDate,
    Instant endDate,
    boolean isActive
) {} 