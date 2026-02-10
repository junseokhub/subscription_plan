package com.subscription.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequestDto {
    private String userName;
    private String password;
    private Long planId;
}
