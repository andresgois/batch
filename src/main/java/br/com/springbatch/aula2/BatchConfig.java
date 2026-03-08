package br.com.springbatch.aula2;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.PlatformTransactionManager;


//@Configuration
public class BatchConfig {

    @Bean
    public Job imprimirOlaJob(JobRepository jobRepository, Step stepOlaMundo) {
        return new JobBuilder("imprimirOlaJob", jobRepository)
                .start(stepOlaMundo)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step stepOlaMundo(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             Tasklet impimirOlaTasket) {

        return new StepBuilder("stepOlaMundo", jobRepository)
                .tasklet(impimirOlaTasket, transactionManager)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet impimirOlaTasket(@Value("#{jobParameters['nome']}") String nome) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println(String.format("Olá, %s!", nome));
                return RepeatStatus.FINISHED;
            }
        };
    }
}
