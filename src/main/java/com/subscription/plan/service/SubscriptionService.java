package com.subscription.plan.service;

import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.dto.SubscriptionResponseDto;
import com.subscription.plan.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final MemberService memberService;

    public List<SubscriptionResponseDto> getSubscriptionsByMemberId(Long memberId) {
        return subscriptionRepository.findByMemberId(memberId).stream()
                .map(SubscriptionResponseDto::from)
                .toList();
    }

    public List<SubscriptionResponseDto> getActiveSubscriptions() {
        return subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE).stream()
                .map(SubscriptionResponseDto::from)
                .toList();
    }
}
