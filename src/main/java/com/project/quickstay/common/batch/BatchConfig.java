package com.project.quickstay.common.batch;

import com.project.quickstay.domain.reservation.entity.Reservation;
import com.project.quickstay.repository.ReservationRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaTransactionManager;

import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final ReservationRepository reservationRepository;
    private final EntityManagerFactory emf;

    private final int chunkSize = 50;

    @Bean
    public Job completeReservationJob(JobRepository jobRepository, Step completedReservationJobStep) {
        return new JobBuilder("completeReservationJob", jobRepository)
                .start(completedReservationJobStep)
                .build();
    }


    @Bean
    public Step completedReservationJobStep(JobRepository jobRepository) {
        return new StepBuilder("completeJob", jobRepository)
                .<Reservation, Reservation>chunk(chunkSize, new JpaTransactionManager(emf))
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public RepositoryItemReader<Reservation> reader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        return new RepositoryItemReaderBuilder<Reservation>()
                .repository(reservationRepository)
                .methodName("findAllReserved")
                .pageSize(chunkSize)
                .sorts(sorts)
                .name("reader")
                .build();
    }

    @Bean
    @StepScope
    public ReservationItemProcessor processor() {
        return new ReservationItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Reservation> writer() {
        return new RepositoryItemWriterBuilder<Reservation>()
                .repository(reservationRepository)
                .build();
    }

}
