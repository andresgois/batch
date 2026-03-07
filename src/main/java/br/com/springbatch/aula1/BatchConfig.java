package br.com.springbatch.aula1;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.annotation.PostConstruct;

@EnableBatchProcessing      // para trazer todas as features do spring batch, é necessário habilita-lo
@Configuration              // necessário ser uma classe de configuração
public class BatchConfig {

    @Autowired
    private JobLauncher jobLauncher;
    
    @Autowired 
    private Job imprimirOlaJob;

    @Bean                   // retorna um job
    public Job imprimirOlaJob(JobRepository jobRepository, Step stepOlaMundo) {
        return new JobBuilder("imprimirOlaJob", jobRepository) // nome do job , o job é dividido em etapas que são chamado de steps, steps são encadeados para contér um lógica maior
                .start(stepOlaMundo)
                .build();
    }

    @Bean
    public Step stepOlaMundo(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager) {

        return new StepBuilder("stepOlaMundo", jobRepository)// Nome do step
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Olá Mundo");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
