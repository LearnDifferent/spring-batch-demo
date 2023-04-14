package com.example.springbatchdemo.o_normal_file_writer;

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
 * 学习写入普通文件的 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
@EnableBatchProcessing
public class NormalFileWriterConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemWriter<Customer> normalFileWriterItemWriter;

    public NormalFileWriterConfig(JobBuilderFactory jobBuilderFactory,
                                  StepBuilderFactory stepBuilderFactory,
                                  @Qualifier("normalFileWriterItemWriter")
                                          ItemWriter<Customer> normalFileWriterItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.normalFileWriterItemWriter = normalFileWriterItemWriter;
    }

    @Bean
    public Job normalFileWriterJob() {
        return jobBuilderFactory
                .get("normalFileWriterJob")
                .start(normalFileWriterStep())
                .build();

    }

    @Bean
    public Step normalFileWriterStep() {
        return stepBuilderFactory
                .get("normalFileWriterStep")
                .<Customer, Customer>chunk(20)
                .writer(normalFileWriterItemWriter)
                .reader(normalFileWriterItemReader())
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> normalFileWriterItemReader() {
        List<Customer> customers = new ArrayList<>();
        long times = 100L;
        for (long i = 0L; i < times; i++) {
            customers.add(new Customer(i, "age" + i, "address" + i));
        }
        return new ListItemReader<>(customers);
    }
}
