package com.subscription.plan.domain;

import com.subscription.plan.common.PlanType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    private int durationMonths;
    private Long originPrice;
    private int discountRate;
    private Long totalPrice;

    @Builder
    public Plan(PlanType planType, int durationMonths) {
        this.planType = planType;
        this.durationMonths = durationMonths;
        this.discountRate = switch (durationMonths) {
            case 6 -> 10;
            case 12 -> 20;
            default -> 0;
        };

        long baseSum = planType.getPrice() * durationMonths;
        this.originPrice = baseSum;
        this.totalPrice = baseSum - (baseSum * this.discountRate / 100);
    }
}