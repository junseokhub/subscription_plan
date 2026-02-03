package com.subscription.plan.repository;

import com.subscription.plan.common.PlanType;
import com.subscription.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>{
    Optional<Plan> findByPlanType(PlanType planType);
    Optional<Plan> findByPlanTypeAndDurationMonths(PlanType planType, int durationMonths);
}
