package br.com.springbatch.aula16_refac.write;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ImprimirWrite {

    @Bean
    protected ItemWriter<String> imprimirRefacWrite() {
        return itens -> itens.forEach(System.out::println);
    }

}
