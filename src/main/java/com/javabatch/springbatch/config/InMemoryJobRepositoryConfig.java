package com.javabatch.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.SimpleJobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.javabatch.springbatch.listener.BatchJobCompletoListener;
import com.javabatch.springbatch.step.BatchProcessor;
import com.javabatch.springbatch.step.BatchReader;
import com.javabatch.springbatch.step.BatchWriter;

/**
 * Configuracao do Spring batch para executar em memoria. Comentar essa classe caso queira que a
 * executacao seja no banco postgresql
 * 
 * @author Edmar
 *
 */
@Configuration
public class InMemoryJobRepositoryConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory() throws Exception {

        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(new ResourcelessTransactionManager());
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean factoryBean) throws Exception {

        try {
            JobRepository jobRepository = factoryBean.getObject();
            return jobRepository;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public JobExplorer jobExplorer(MapJobRepositoryFactoryBean factory) {

        return new SimpleJobExplorer(factory.getJobInstanceDao(), factory.getJobExecutionDao(), factory.getStepExecutionDao(),
                factory.getExecutionContextDao());
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);

        return jobLauncher;
    }

    @Bean
    public Job job(JobRepository jobRepository) {
        return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).repository(jobRepository).listener(listener()).flow(step1()).end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<String, String>chunk(1).reader(new BatchReader()).processor(new BatchProcessor())
                .writer(new BatchWriter()).build();
    }

    /**
     * Config do listener
     * 
     * @return
     */
    @Bean
    public JobExecutionListener listener() {
        return new BatchJobCompletoListener();
    }

}
