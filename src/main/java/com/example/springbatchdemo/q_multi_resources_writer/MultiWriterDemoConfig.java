package com.example.springbatchdemo.q_multi_resources_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置多个 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
@EnableBatchProcessing
public class MultiWriterDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemWriter<Customer> multiWriterItemWriter;

    public MultiWriterDemoConfig(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory,
                                 @Qualifier("multiItemWriter") ItemWriter<Customer> multiWriterItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.multiWriterItemWriter = multiWriterItemWriter;
    }

    @Bean
    public Job multiWriterJob() {
        return jobBuilderFactory
                .get("multiWriterJob")
                .start(multiWriterStep())
                .build();
    }

    @Bean
    public Step multiWriterStep() {
        return stepBuilderFactory
                .get("multiWriterStep")
                .<Customer, Customer>chunk(20)
                .writer(multiWriterItemWriter)
                .reader(multiWriterItemReader())
                .build();
    }

    @Bean
    public ItemReader<Customer> multiWriterItemReader() {
        List<Customer> list = new ArrayList<>();
        long times = 50L;
        for (long i = 0L; i < times; i++) {
            list.add(new Customer(i, "name" + i, "address" + i));
        }
        return new ListItemReader<>(list);
    }
}
