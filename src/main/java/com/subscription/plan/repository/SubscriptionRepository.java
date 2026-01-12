package com.subscription.plan.repository;

import com.subscription.plan.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<List<Subscription>> findByMemberId(Long memberId);
    Optional<Subscription> findByStatus(SubscriptionRepository status);
}
