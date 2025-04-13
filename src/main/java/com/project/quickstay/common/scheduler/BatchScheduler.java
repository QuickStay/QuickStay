package com.project.quickstay.common.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final Job completeReservationJob;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void runBatchJob() {
        log.info("날짜: {}, 시간: {}, 배치 스케줄러 실행", LocalDate.now(), LocalTime.now());
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("runDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")))
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(completeReservationJob, params);
            log.info("Job Status: {}", execution.getStatus());
        } catch (Exception e) {
            log.error("Job 실행 중 오류 발생: {}", e.getMessage());
        }
    }
}
