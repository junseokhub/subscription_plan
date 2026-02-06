package com.subscription.plan.batch;

import com.subscription.plan.batch.listener.StatisticsListener;
import com.subscription.plan.batch.steps.StatisticsStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SpringBatchJob {

    private final JobRepository jobRepository;
//    private final JobBuilderFactory jobBuilderFactory;
    private final StatisticsListener listener;
    private final StatisticsStep statisticsStep;


    @Bean
    public Job statisticsJob() {
        return new JobBuilder("statisticsJob", jobRepository)
                .listener(listener)
                .start(statisticsStep.cleanupStep())
                .next(statisticsStep.baseStatisticsStep())
                .build();
    }
}
