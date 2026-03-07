package br.com.springbatch.aula1;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

//@EnableBatchProcessing

/**
 * 1. Remova @EnableBatchProcessing
 *
 * No Spring Batch 5 com Spring Boot 3, o @EnableBatchProcessing não é mais necessário e pode causar conflitos. O Spring Boot configura automaticamente o batch quando detecta a dependência.
 */
@Configuration
public class BatchConfig {

    @Bean
    public Job imprimirOlaJob(JobRepository jobRepository, Step stepOlaMundo) {
        return new JobBuilder("imprimirOlaJob", jobRepository)
                .start(stepOlaMundo)
                .build();
    }

    @Bean
    public Step stepOlaMundo(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {

        return new StepBuilder("stepOlaMundo", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Olá Mundo");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
