package com.subscription.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlanChangeRequestDto {
    private Long memberId;
    private Long subscriptionId;
    private Long nextPlanId;
}