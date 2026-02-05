package com.subscription.plan.dto;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Subscription;

import java.time.LocalDateTime;

public record SubscriptionResponseDto(
        Long id,
        Long memberId,
        int cycle,
        boolean autoRenewal,
        SubscriptionStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        PlanType planType
) {
    public static SubscriptionResponseDto from(Subscription s) {
        return new SubscriptionResponseDto(
                s.getId(),
                s.getMember().getId(),
                s.getCycle(),
                s.isAutoRenewal(),
                s.getStatus(),
                s.getStartDate(),
                s.getEndDate(),
                s.getPlan().getPlanType()
        );
    }
}