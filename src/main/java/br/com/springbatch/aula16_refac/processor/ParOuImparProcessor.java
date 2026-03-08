package br.com.springbatch.aula16_refac.processor;

import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ParOuImparProcessor {
    @Bean
    protected FunctionItemProcessor<Integer, String> parOuImparRefacProcessor() {
        return new FunctionItemProcessor<Integer, String>(
                item -> item % 2 == 0 ? String.format("Item %s é Par", item):
                        String.format("Item %s é Impar", item)
        );
    }
}
