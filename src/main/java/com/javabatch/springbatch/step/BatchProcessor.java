package com.javabatch.springbatch.step;

import org.springframework.batch.item.ItemProcessor;

public class BatchProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String content) throws Exception {
        System.out.println("METODO PROCESS. PROCESSANDO DADOS");

        return content.toUpperCase();
    }

}
