package com.subscription.plan.batch.listener;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
public class StatisticsListener implements JobExecutionListener {
    @Override
    public void beforeJob(@NonNull JobExecution jobExecution) {
        log.info("### 배치 Job 시작: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(@NonNull JobExecution jobExecution) {
        long duration = Duration.between(jobExecution.getStartTime(), jobExecution.getEndTime()).toMillis();
        log.info("### 배치 Job 종료: {} (상태: {}, 소요시간: {}ms)",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStatus(),
                duration);

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("!!! 배치 실행 실패 - DB 확인 필요");
        }
    }
}
