package com.subscription.plan.batch.processor;

import com.subscription.plan.domain.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AutoRenewalProcessor implements ItemProcessor<Subscription, Subscription> {

    @Override
    public Subscription process(Subscription sub) throws Exception {
        try {
            LocalDateTime beforeEndDate = sub.getEndDate();
            sub.renew();

            if (sub.getId() % 50 == 1) {
                log.info("""
                #### [Subscription Renew ] ####
                {
                  "id": {},
                  "userName": "{}",
                  "planType": "{}",
                  "endDateBefore": "{}",
                  "cycle": "{}",
                  "endDateAfter": "{}",
                  "status": "{}"
                }
                #####################################""",
                        sub.getId(),
                        sub.getMember().getUserName(),
                        sub.getPlan().getPlanType(),
                        beforeEndDate,
                        sub.getCycle(),
                        sub.getEndDate(),
                        sub.getStatus()
                );
            }
            return sub;
        } catch (Exception e) {
            log.error("구독 갱신 실패: 구독 ID={}", sub.getId(), e);
            throw e; // skip 정책에 따라 처리
        }
    }
}