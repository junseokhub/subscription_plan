package com.subscription.plan.service;

import com.subscription.plan.domain.Plan;
import com.subscription.plan.common.PlanType;
import com.subscription.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

    @Transactional
    public Long createPlan(PlanType type, int durationMonths) {
        planRepository.findByPlanTypeAndDurationMonths(type, durationMonths)
                .ifPresent(p -> { throw new IllegalStateException("Exists"); });

        Plan plan = Plan.builder()
                .planType(type)
                .durationMonths(durationMonths)
                .build();

        return planRepository.save(plan).getId();
    }

    public Plan getPlanById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found"));
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }
}