package com.subscription.plan.service;

import com.subscription.plan.dto.PlanChangeRequestDto;
import com.subscription.plan.dto.SubscriptionRequestDto;
import com.subscription.plan.dto.SubscriptionResponseDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponseDto createSubscription(SubscriptionRequestDto dto);
    void updateSubscriptionReservation(PlanChangeRequestDto dto);
    List<SubscriptionResponseDto> getSubscriptionsByMemberId(Long memberId);
    List<SubscriptionResponseDto> getActiveSubscriptions();
    void cancelSubscriptionReservation(Long subscriptionId);
}
