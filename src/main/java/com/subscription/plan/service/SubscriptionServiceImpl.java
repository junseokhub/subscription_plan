package com.subscription.plan.service;

import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Member;
import com.subscription.plan.domain.Plan;
import com.subscription.plan.domain.Subscription;
import com.subscription.plan.dto.MemberResponseDto;
import com.subscription.plan.dto.PlanChangeRequestDto;
import com.subscription.plan.dto.SubscriptionRequestDto;
import com.subscription.plan.dto.SubscriptionResponseDto;
import com.subscription.plan.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;
    private final MemberService memberService;
    private final PlanService planService;

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

    @Override
    @Transactional
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto dto) {
        MemberResponseDto memberDto = memberService.findMember(dto.getUserName());
        Plan plan = planService.getPlanById(dto.getPlanId());

        Member member = Member.builder()
                .id(memberDto.id())
                .userName(memberDto.userName())
                .build();

        Subscription subscription = Subscription.builder()
                .member(member)
                .plan(plan)
                .cycle(1)
                .autoRenewal(true)
                .build();

        return SubscriptionResponseDto.from(subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional
    public void updateSubscriptionReservation(PlanChangeRequestDto dto) {
        Subscription subscription = subscriptionRepository.findById(dto.getSubscriptionId())
                .orElseThrow(() -> new EntityNotFoundException("구독 정보를 찾을 수 없습니다."));

        if (!subscription.getMember().getId().equals(dto.getMemberId())) {
            throw new SecurityException("해당 구독을 변경할 권한이 없습니다.");
        }

        Plan nextPlan = planService.getPlanById(dto.getNextPlanId());

        subscription.reservePlanChange(nextPlan);
    }

    @Transactional
    public void cancelSubscriptionReservation(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("구독 정보를 찾을 수 없습니다."));

        subscription.cancelPlanChange();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void processAutoRenewal() {
        LocalDateTime now = LocalDateTime.now();
        List<Subscription> targets = subscriptionRepository
                .findAllByStatusAndAutoRenewalTrueAndEndDateBefore(SubscriptionStatus.ACTIVE, now);

        for (Subscription sub : targets) {
            try {
                sub.renew();
                log.info("구독 갱신 완료: 유저={}, 적용된 플랜={}",
                        sub.getMember().getUserName(), sub.getPlan().getPlanType());
            } catch (Exception e) {
                log.error("구독 갱신 실패: 구독 ID={}", sub.getId(), e);
            }
        }
    }
}