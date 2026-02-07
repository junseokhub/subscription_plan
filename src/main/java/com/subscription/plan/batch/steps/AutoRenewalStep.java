package com.subscription.plan.batch.steps;

import com.subscription.plan.domain.Subscription;
import com.subscription.plan.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AutoRenewalStep {
    private final JobRepository jobRepository;
    //    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;
    private final RepositoryItemReader<Subscription> autoRenewalReader;
    private final SubscriptionRepository subscriptionRepository;
    private final ItemProcessor<Subscription, Subscription> autoRenewalProcessor;


    @Bean
    public Step autoRenewalStep() {
        return new StepBuilder("autoRenewalStep", jobRepository)
                .<Subscription, Subscription>chunk(50)
                .transactionManager(transactionManager)
                .reader(autoRenewalReader)
                .processor(autoRenewalProcessor)
                .writer(chunk -> {
                    log.info(">>>>> [Step 3] 자동 갱신 저장 중 (size: {})", chunk.size());
                    subscriptionRepository.saveAll(chunk);
                })
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
                .build();
    }
}
