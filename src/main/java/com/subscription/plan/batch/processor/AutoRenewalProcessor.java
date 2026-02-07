package com.subscription.plan.batch.processor;

import com.subscription.plan.domain.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoRenewalProcessor implements ItemProcessor<Subscription, Subscription> {

    @Override
    public Subscription process(Subscription sub) throws Exception {
        try {
            sub.renew(); // 구독 갱신
            log.info("구독 갱신 완료: 유저={}, 플랜={}",
                    sub.getMember().getUserName(),
                    sub.getPlan().getPlanType());
            return sub;
        } catch (Exception e) {
            log.error("구독 갱신 실패: 구독 ID={}", sub.getId(), e);
            throw e; // skip 정책에 따라 처리
        }
    }
}