package com.subscription.plan.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class SpringScheduler {

    private final JobOperator jobOperator;
    private final Job statisticsJob;
    private final Job autoRenewalJob;
//    private final JobLauncher jobLauncher;

    @Scheduled(cron = "*/50 * * * * ?")
    public void runStatisticsJob() {
        try {
            log.info("배치 스케줄 시작");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("runTime", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobOperator.start(statisticsJob, jobParameters);
//            JobExecution jobExecution = jobLauncher.run(statisticsJob, jobParameters);
            log.info("Batch executed, status: {}", jobExecution.getStatus());

        } catch (Exception e) {
            log.error("❌ 통계 배치 실행 실패", e);
        }
    }

//    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = "*/50 * * * * ?")
    public void runAutoRenewalJob() {
        try {
            log.info("자동 갱신");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("runTime", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution jobExecution = jobOperator.start(autoRenewalJob, jobParameters);

            log.info("Batch executed, status: {}", jobExecution.getStatus());
        } catch (Exception e) {
            log.error("❌ 통계 배치 실행 실패", e);
        }
    }
}
