package com.novademy.application.services;

import com.novademy.contracts.requests.subscription.SubscriptionRequest;
import com.novademy.contracts.responses.subscription.SubscriptionResponse;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    List<SubscriptionResponse> getActiveByUserId(UUID userId);
    SubscriptionResponse subscribe(SubscriptionRequest request);
} 