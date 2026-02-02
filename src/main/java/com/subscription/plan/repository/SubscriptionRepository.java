package com.subscription.plan.repository;

import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByMemberId(Long memberId);

    List<Subscription> findByStatus(SubscriptionStatus status);
}
