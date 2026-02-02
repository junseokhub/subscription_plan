package com.subscription.plan.service;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Member;
import com.subscription.plan.domain.Subscription;
import com.subscription.plan.dto.SubscriptionResponseDto;
import com.subscription.plan.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    // 테스트용 상수 설정 (Magic Number/String 방지)
    private static final Long TEST_MEMBER_ID = 1L;

    @Test
    @DisplayName("멤버 ID로 구독 목록을 조회하면 DTO 리스트로 변환되어 반환된다")
    void getSubscriptionsByMemberId_Success() {
        Subscription activeSub = createSubscription(TEST_MEMBER_ID, PlanType.BASIC, SubscriptionStatus.ACTIVE);
        given(subscriptionRepository.findByMemberId(TEST_MEMBER_ID))
                .willReturn(List.of(activeSub));

        List<SubscriptionResponseDto> result = subscriptionService.getSubscriptionsByMemberId(TEST_MEMBER_ID);

        assertThat(result).hasSize(1);

        assertThat(result.get(0).planType()).isEqualTo(PlanType.BASIC);
        assertThat(result.get(0).status()).isEqualTo(SubscriptionStatus.ACTIVE);

        assertThat(result).extracting(SubscriptionResponseDto::planType, SubscriptionResponseDto::status)
                .containsExactly(tuple(PlanType.BASIC, SubscriptionStatus.ACTIVE));
    }

    @Test
    @DisplayName("ACTIVE 상태의 구독만 필터링하여 조회한다")
    void getActiveSubscriptions_Success() {
        Subscription activeSub = createSubscription(TEST_MEMBER_ID, PlanType.PREMIUM, SubscriptionStatus.ACTIVE);
        given(subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE))
                .willReturn(List.of(activeSub));

        List<SubscriptionResponseDto> result = subscriptionService.getActiveSubscriptions();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).status()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(result.get(0).planType()).isEqualTo(PlanType.PREMIUM);

        assertThat(result)
                .extracting(SubscriptionResponseDto::status, SubscriptionResponseDto::planType)
                .containsExactly(tuple(SubscriptionStatus.ACTIVE, PlanType.PREMIUM));
    }

    private Long idCounter = 1L;

    private Subscription createSubscription(Long memberId, PlanType planType, SubscriptionStatus status) {
        Member member = Member.builder()
                .id(memberId)
                .userName("testUser")
                .build();

        return Subscription.builder()
                .id(idCounter++)
                .member(member)
                .planType(planType)
                .status(status)
                .price(planType == PlanType.BASIC ? 4900L : 9900L)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusYears(1))
                .cycle(1)
                .autoRenewal(true)
                .build();
    }
}
