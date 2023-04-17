package com.example.springbatchdemo.w_error_listener;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这里演示发生错误时，如果跳过错误，需要使用的监听器（重试也是一样的原理）
 *
 * @author zhou
 * @date 2023/4/17
 */
@Configuration
@EnableBatchProcessing
public class ErrorListenerDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * 跳过错误时使用的监听器
     */
    private final SkipListener<Integer, Integer> customSkipListener;

    public ErrorListenerDemoConfig(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory,
                                   SkipListener<Integer, Integer> customSkipListener) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.customSkipListener = customSkipListener;
    }

    @Bean
    public Job errorListenerJob() {
        return jobBuilderFactory
                .get("errorListenerJob")
                .start(errorListenerStep())
                .build();
    }

    @Bean
    public Step errorListenerStep() {
        return stepBuilderFactory
                .get("errorListenerStep")
                .<Integer, Integer>chunk(20)
                .reader(errorListenerReader())
                .processor(errorListenerProcessor())
                .writer(errorListenerWriter())
                // 这里演示跳过错误，重试也是一样的
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(Integer.MAX_VALUE)
                // 这里就是监听器，用于监听发生的错误
                .listener(customSkipListener)
                .build();
    }

    @Bean
    public ItemWriter<Integer> errorListenerWriter() {
        return new ListItemWriter<>();
    }

    @Bean
    public ItemProcessor<Integer, Integer> errorListenerProcessor() {
        return item -> {
            if (item % 2 == 0) {
                throw new RuntimeException("Processor 故意报错");
            }
            return item;
        };
    }

    @Bean
    public ItemReader<Integer> errorListenerReader() {
        List<Integer> list = new ArrayList<>();
        int times = 100;
        for (int i = 0; i < times; i++) {
            list.add(i);
        }
        return new ListItemReader<>(list);
    }

}
