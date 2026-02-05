package com.subscription.plan.controller;

import com.subscription.plan.dto.SubscriptionRequestDto;
import com.subscription.plan.dto.SubscriptionResponseDto;
import com.subscription.plan.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/create")
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto requestDto) {
        return subscriptionService.createSubscription(requestDto);
    }

    @GetMapping()
    public List<SubscriptionResponseDto> getSubscriptionByMemberId(@PathVariable Long memberId) {
        return subscriptionService.getSubscriptionsByMemberId(memberId);
    }

    @GetMapping("/active")
    public List<SubscriptionResponseDto> getActiveSubscriptions() {
        return subscriptionService.getActiveSubscriptions();
    }

    @PostMapping("/cancel")
    public void cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscriptionReservation(subscriptionId);
    }
}
