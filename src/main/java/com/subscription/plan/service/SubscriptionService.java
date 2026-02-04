package com.subscription.plan.service;

import com.subscription.plan.dto.PlanChangeRequestDto;
import com.subscription.plan.dto.SubscriptionRequestDto;
import com.subscription.plan.dto.SubscriptionResponseDto;

public interface SubscriptionService {
    SubscriptionResponseDto createSubscription(SubscriptionRequestDto dto);
    void updateSubscriptionReservation(PlanChangeRequestDto dto);
}
