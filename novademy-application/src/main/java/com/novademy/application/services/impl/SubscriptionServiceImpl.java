package com.novademy.application.services.impl;

import com.novademy.application.entities.Subscription;
import com.novademy.application.mapping.ContractMapping;
import com.novademy.application.repositories.SubscriptionRepository;
import com.novademy.application.services.SubscriptionService;
import com.novademy.contracts.requests.subscription.SubscriptionRequest;
import com.novademy.contracts.responses.subscription.SubscriptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public List<SubscriptionResponse> getActiveByUserId(UUID userId) {
        return subscriptionRepository.findByUserId(userId)
            .stream()
            .filter(s -> LocalDateTime.now().isAfter(s.getStartDate()) && LocalDateTime.now().isBefore(s.getEndDate()))
            .map(ContractMapping::toSubscriptionResponse)
            .collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponse subscribe(SubscriptionRequest request) {
        Subscription sub = ContractMapping.toSubscription(request);
        sub.setStartDate(LocalDateTime.now());
        sub.setEndDate(LocalDateTime.now().plusYears(1));
        subscriptionRepository.save(sub);
        return ContractMapping.toSubscriptionResponse(sub);
    }
} 