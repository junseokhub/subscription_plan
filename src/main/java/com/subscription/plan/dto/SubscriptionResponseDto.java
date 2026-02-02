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
        PlanType planType,
        Long price,
        SubscriptionStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static SubscriptionResponseDto from(Subscription s) {
        return new SubscriptionResponseDto(
                s.getId(),
                s.getMember().getId(),
                s.getCycle(),
                s.isAutoRenewal(),
                s.getPlanType(),
                s.getPrice(),
                s.getStatus(),
                s.getStartDate(),
                s.getEndDate()
        );
    }
}