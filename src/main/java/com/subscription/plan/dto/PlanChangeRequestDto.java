package com.subscription.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanChangeRequestDto {
    private Long memberId;
    private Long subscriptionId;
    private Long nextPlanId;
}