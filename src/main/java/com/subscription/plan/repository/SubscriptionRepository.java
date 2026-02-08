package com.subscription.plan.repository;

import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByMemberId(Long memberId);

    List<Subscription> findByStatus(SubscriptionStatus status);

    Page<Subscription> findAllByStatusAndAutoRenewalTrueAndEndDateBefore(
            SubscriptionStatus status,
            LocalDateTime dateTime,
            Pageable pageable
    );

    @Query("SELECT s FROM Subscription s WHERE s.status = :status AND s.autoRenewal = true AND s.endDate < :dateTime")
    Page<Subscription> findAutoRenewals(@Param("status") SubscriptionStatus status,
                                        @Param("dateTime") LocalDateTime dateTime,
                                        Pageable pageable);
}
