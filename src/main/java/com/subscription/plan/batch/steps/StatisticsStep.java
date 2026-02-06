package com.subscription.plan.batch.steps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StatisticsStep {

    private final JobRepository jobRepository;
//    private final StepBuilderFactory stepBuilderFactory;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> [Step 1] 통계 데이터 초기화 시작");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
//                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Step baseStatisticsStep() {
        return new StepBuilder("baseStatisticsStep", jobRepository)
//                .<Object, Object>chunk(10)
                .chunk(10)
                .transactionManager(transactionManager)
                .reader(() -> {
                    log.info(">>>>> [Step 2] 데이터 읽는 중...");
                    return null;
                })
                .writer(chunk -> {
                    log.info(">>>>> [Step 2] 데이터 저장 중 (size: {})", chunk.size());
                })
                .build();
    }
}
