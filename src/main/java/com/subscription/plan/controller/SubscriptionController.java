package com.subscription.plan.controller;

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

    @GetMapping("/member/{memberId}")
    public List<SubscriptionResponseDto> findByMember(@PathVariable Long memberId) {
        return subscriptionService.getSubscriptionsByMemberId(memberId);
    }

    @GetMapping("/active")
    public List<SubscriptionResponseDto> findActiveSubscriptions() {
        return subscriptionService.getActiveSubscriptions();
    }
}
