package com.subscription.plan.batch.reader;

import com.subscription.plan.common.SubscriptionStatus;
import com.subscription.plan.domain.Subscription;
import com.subscription.plan.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.batch.infrastructure.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AutoRenewalReader {
    private final SubscriptionRepository subscriptionRepository;


    @Bean
    public RepositoryItemReader<Subscription> autoRenewalReader() {
        return new RepositoryItemReaderBuilder<Subscription>()
                .name("autoRenewalReader")
                .repository(subscriptionRepository)
                .methodName("findAllByStatusAndAutoRenewalTrueAndEndDateBefore")
                .arguments(SubscriptionStatus.ACTIVE, LocalDateTime.now())
                .pageSize(50)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }
}
