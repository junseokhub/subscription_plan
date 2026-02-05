package com.subscription.plan.service;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.domain.Member;
import com.subscription.plan.domain.Plan;
import com.subscription.plan.domain.Subscription;
import com.subscription.plan.dto.MemberResponseDto;
import com.subscription.plan.dto.PlanChangeRequestDto;
import com.subscription.plan.dto.SubscriptionRequestDto;
import com.subscription.plan.dto.SubscriptionResponseDto;
import com.subscription.plan.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private PlanService planService;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private final Map<Long, Plan> planStorage = new HashMap<>();

    @BeforeEach
    void setUp() {
        long idCounter = 1L;
        for (PlanType type : PlanType.values()) {
            for (int m : List.of(1, 6, 12)) {
                Plan plan = Plan.builder()
                        .planType(type)
                        .durationMonths(m)
                        .build();

                ReflectionTestUtils.setField(plan, "id", idCounter);

                planStorage.put(idCounter, plan);
                idCounter++;
            }
        }

        lenient().when(planService.getPlanById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            Plan found = planStorage.get(id);
            if (found == null) throw new RuntimeException("Test Plan not found for ID: " + id);
            return found;
        });
    }

    @Test
    @DisplayName("구독 생성: 12개월 프리미엄 플랜(ID 9번)으로 구독이 생성되는지 확인")
    void createSubscription_Success() {
        Long targetPlanId = 9L;
        SubscriptionRequestDto request = new SubscriptionRequestDto("userA", targetPlanId);
        MemberResponseDto memberDto = new MemberResponseDto(1L, "userA");

        given(memberService.findMember("userA")).willReturn(memberDto);
        given(subscriptionRepository.save(any(Subscription.class))).willAnswer(inv -> {
            Subscription sub = inv.getArgument(0);
            ReflectionTestUtils.setField(sub, "id", 1L);

            return sub;
        });
        SubscriptionResponseDto response = subscriptionService.createSubscription(request);
        log.info("response: {}", response);
        assertThat(response).isNotNull();

        Plan appliedPlan = planStorage.get(targetPlanId);
        assertThat(appliedPlan.getPlanType()).isEqualTo(PlanType.PREMIUM);
        assertThat(appliedPlan.getDiscountRate()).isEqualTo(20);
    }

    @Test
    @DisplayName("구독 플랜 예약 변경: 1번 플랜에서 6번 플랜으로 정상 변경 예약")
    void updateSubscriptionReservation_Success() {
        Long memberId = 1L;
        Long subId = 1L;
        Long nextPlanId = 6L;

        Member member = Member.builder().build();
        ReflectionTestUtils.setField(member, "id", memberId);

        Subscription subscription = Subscription.builder()
                .member(member)
                .plan(planStorage.get(1L))
                .build();
        ReflectionTestUtils.setField(subscription, "id", subId);

        given(subscriptionRepository.findById(subId)).willReturn(Optional.of(subscription));

        PlanChangeRequestDto request = new PlanChangeRequestDto(memberId, subId, nextPlanId);

        subscriptionService.updateSubscriptionReservation(request);
        Plan resultPlan = (Plan) ReflectionTestUtils.getField(subscription, "pendingPlan");

        assertThat(resultPlan).isNotNull();
        assertThat(resultPlan.getId()).isEqualTo(nextPlanId);
        assertThat(resultPlan.getPlanType()).isEqualTo(planStorage.get(nextPlanId).getPlanType());

        verify(subscriptionRepository, times(1)).findById(subId);
    }

    @Test
    @DisplayName("자동 갱신: 만료된 구독 갱신 시 로그와 renew 호출 확인")
    void processAutoRenewal_Success() {
        Member member = Member.builder().userName("userA").build();
        Subscription activeSub = Subscription.builder()
                .member(member)
                .plan(planStorage.get(1L))
                .build();

        Subscription spySub = spy(activeSub);

        given(subscriptionRepository.findAllByStatusAndAutoRenewalTrueAndEndDateBefore(any(), any()))
                .willReturn(List.of(spySub));

        subscriptionService.processAutoRenewal();

        verify(spySub, times(1)).renew();
    }
}