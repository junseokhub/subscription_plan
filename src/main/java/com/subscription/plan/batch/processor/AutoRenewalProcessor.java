package com.subscription.plan.batch.processor;

import com.subscription.plan.batch.log.SubscriptionRenewalLogHelper;
import com.subscription.plan.domain.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoRenewalProcessor implements ItemProcessor<Subscription, Subscription> {

    @Override
    public Subscription process(Subscription sub) {

        boolean sampleLog = sub.getId() % 50 == 1;

        var before = sampleLog
                ? SubscriptionRenewalLogHelper.Snapshot.from(sub)
                : null;

        try {
            sub.renew();

            if (sampleLog) {
                var after = SubscriptionRenewalLogHelper.Snapshot.from(sub);
                SubscriptionRenewalLogHelper.logRenewal(before, after);
            }

            return sub;

        } catch (Exception e) {
            log.error("구독 자동 갱신 실패 id={}", sub.getId(), e);
            throw e;
        }
    }
}
