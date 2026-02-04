package com.subscription.plan.controller;

import com.subscription.plan.dto.SubscriptionRequestDto;
import com.subscription.plan.dto.SubscriptionResponseDto;
import com.subscription.plan.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto requestDto) {
        return subscriptionService.createSubscription(requestDto);
    }
}
