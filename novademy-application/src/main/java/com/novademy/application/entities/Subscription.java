package com.novademy.application.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", insertable = false, updatable = false)
    private User user;

    @NotNull
    @Column(name = "PackageId", nullable = false)
    private UUID packageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PackageId", insertable = false, updatable = false)
    private Package pack;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Transient
    public boolean isActive() {
        return LocalDateTime.now().isAfter(startDate) && LocalDateTime.now().isBefore(endDate);
    }
} 