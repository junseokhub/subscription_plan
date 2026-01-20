package com.subscription.plan.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StatisticsJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job statisticsJob() {
        return new JobBuilder("statisticsJob", jobRepository)
                .listener(jobExecutionListener()) // Job 실행 로그 리스너
                .start(cleanupStep())
                .next(baseStatisticsStep())
                .build();
    }

    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> [Step 1] 통계 데이터 초기화 시작");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step baseStatisticsStep() {
        return new StepBuilder("baseStatisticsStep", jobRepository)
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

    private JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(@NonNull JobExecution jobExecution) {
                log.info("### 배치 Job 시작: {}", jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(@NonNull JobExecution jobExecution) {
                assert jobExecution.getStartTime() != null;
                long duration = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis();
                log.info("### 배치 Job 종료: {} (상태: {}, 소요시간: {}ms)",
                        jobExecution.getJobInstance().getJobName(),
                        jobExecution.getStatus(),
                        duration);

                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    log.error("!!! 배치 실행 실패 - DB의 BATCH_JOB_EXECUTION 테이블 확인 필요");
                }
            }
        };
    }
}