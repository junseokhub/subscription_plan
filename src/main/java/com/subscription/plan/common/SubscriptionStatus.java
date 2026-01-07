package com.subscription.plan.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubscriptionStatus {

    ACTIVE("Active"),
    CANCELED("Canceled"),
    EXPIRED("Expired");

    private final String description;

    public boolean isUsable() {
        return this == ACTIVE || this == CANCELED;
    }

    public boolean isChurned() {
        return this == CANCELED || this == EXPIRED;
    }
}