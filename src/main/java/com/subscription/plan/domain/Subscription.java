package com.subscription.plan.domain;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.common.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int cycle;
    private boolean autoRenewal;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    private Long price;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime cancelDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Enumerated(EnumType.STRING)
    private PlanType pendingPlanType;
    private LocalDateTime changeEffectiveDate;

    public void changePlan(PlanType newPlan, Long newPrice) {
        this.pendingPlanType = newPlan;
        this.price = newPrice;
    }

    public boolean isRenewal() {
        return this.cycle > 1;
    }
}