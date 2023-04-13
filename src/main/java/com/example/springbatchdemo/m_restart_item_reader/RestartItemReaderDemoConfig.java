package com.example.springbatchdemo.m_restart_item_reader;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhou
 * @date 2023/4/12
 */
@Configuration
@EnableBatchProcessing
public class RestartItemReaderDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RestartItemReaderDemoItemWriter restartItemReaderDemoItemWriter;
    private final RestartItemReaderDemoStreamReader reader;

    public RestartItemReaderDemoConfig(JobBuilderFactory jobBuilderFactory,
                                       StepBuilderFactory stepBuilderFactory,
                                       @Qualifier("restartItemReaderDemoItemWriter")
                                               RestartItemReaderDemoItemWriter restartItemReaderDemoItemWriter,
                                       @Qualifier("restartItemReaderDemoStreamReader") RestartItemReaderDemoStreamReader reader) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.restartItemReaderDemoItemWriter = restartItemReaderDemoItemWriter;
        this.reader = reader;
    }

    @Bean
    public Job restartItemReaderDemoJob() {
        return jobBuilderFactory
                .get("restartItemReaderDemoJob")
                .start(restartItemReaderDemoStep())
                .build();
    }

    @Bean
    public Step restartItemReaderDemoStep() {
        return stepBuilderFactory
                .get("restartItemReaderDemoStep")
                .<Customer, Customer>chunk(2)
                .reader(reader)
                .writer(restartItemReaderDemoItemWriter)
                .build();
    }

}
