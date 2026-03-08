package br.com.springbatch.aula16_refac.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImprimirParImparStepConfig {

    @Bean
    public Step imprimirParImparStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     IteratorItemReader<Integer> contaAteDezRefacReader,
                                     FunctionItemProcessor<Integer, String> parOuImparRefacProcessor,
                                     ItemWriter<String> imprimirRefacWrite
                                     ) {

        return new StepBuilder("imprimirParImparStep", jobRepository)
                .<Integer, String>chunk(10, transactionManager)
                .reader(contaAteDezRefacReader)
                .processor(parOuImparRefacProcessor)
                .writer(imprimirRefacWrite)
                .build();
    }

}
