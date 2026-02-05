package com.subscription.plan.domain;

import com.subscription.plan.common.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pending_plan_id")
    private Plan pendingPlan;

    private int cycle;
    private boolean autoRenewal;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime cancelDate;
    private LocalDateTime changeEffectiveDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Builder
    public Subscription(Member member, Plan plan, boolean autoRenewal, int cycle) {
        this.member = member;
        this.plan = plan;
        this.cycle = cycle;
        this.autoRenewal = autoRenewal;
        this.startDate = LocalDateTime.now();
        this.endDate = this.startDate.plusMonths(plan.getDurationMonths());
        this.status = SubscriptionStatus.ACTIVE;
    }

    public void renew() {
        if (this.status != SubscriptionStatus.ACTIVE || !this.autoRenewal) return;

        if (this.pendingPlan != null && LocalDateTime.now().isAfter(changeEffectiveDate)) {
            this.plan = this.pendingPlan;
            this.pendingPlan = null;
            this.changeEffectiveDate = null;
        }

        this.cycle += 1;
        this.startDate = this.endDate;
        this.endDate = this.startDate.plusMonths(this.plan.getDurationMonths());
    }

    public void reservePlanChange(Plan nextPlan) {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new IllegalStateException("활성화된 구독만 플랜 변경이 가능합니다.");
        }

        if (this.plan.getId().equals(nextPlan.getId())) {
            throw new IllegalArgumentException("현재와 동일한 플랜으로 변경 예약할 수 없습니다.");
        }

        this.pendingPlan = nextPlan;
        this.changeEffectiveDate = this.endDate;
    }

    public void cancelPlanChange() {
        this.pendingPlan = null;
        this.changeEffectiveDate = null;
    }
}