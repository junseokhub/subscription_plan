package com.subscription.plan.batch.log;

import com.subscription.plan.domain.Subscription;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public final class SubscriptionRenewalLogHelper {

    private SubscriptionRenewalLogHelper() {
    }

    public record Snapshot(
            Long id,
            String userName,
            String planType,
            String pendingPlanType,
            int cycle,
            boolean autoRenewal,
            LocalDateTime startDate,
            LocalDateTime endDate,
            LocalDateTime cancelDate,
            LocalDateTime changeEffectiveDate,
            String status
    ) {
        public static Snapshot from(Subscription sub) {
            return new Snapshot(
                    sub.getId(),
                    sub.getMember().getUserName(),
                    sub.getPlan().getPlanType().name(),
                    sub.getPendingPlan() != null
                            ? sub.getPendingPlan().getPlanType().name()
                            : null,
                    sub.getCycle(),
                    sub.isAutoRenewal(),
                    sub.getStartDate(),
                    sub.getEndDate(),
                    sub.getCancelDate(),
                    sub.getChangeEffectiveDate(),
                    sub.getStatus().name()
            );
        }
    }

    public static void logRenewal(Snapshot before, Snapshot after) {
        log.info("""
            #### [Subscription Auto Renew] ####
            {{
              "id": {},
              "userName": "{}",
              "planType": "{}",
              "pendingPlanType": "{}",
              "cycle": {{ "before": {}, "after": {} }},
              "autoRenewal": {},
              "startDate": "{}",
              "endDate": {{ "before": "{}", "after": "{}" }},
              "cancelDate": "{}",
              "changeEffectiveDate": "{}",
              "status": {{ "before": "{}", "after": "{}" }}
            }}
            ###################################
            """,
                before.id(),
                before.userName(),
                before.planType(),
                before.pendingPlanType(),
                before.cycle(),
                after.cycle(),
                after.autoRenewal(),
                before.startDate(),
                before.endDate(),
                after.endDate(),
                after.cancelDate(),
                after.changeEffectiveDate(),
                before.status(),
                after.status()
        );
    }
}
