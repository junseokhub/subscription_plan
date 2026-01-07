package com.subscription.plan.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlanType {

    BASIC(5500L, "Basic"),
    STANDARD(8500L, "Standard"),
    PREMIUM(10000L, "Premium");

    private final long price;
    private final String label;
}