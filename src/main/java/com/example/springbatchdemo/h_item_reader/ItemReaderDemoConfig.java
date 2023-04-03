package com.example.springbatchdemo.h_item_reader;

import java.util.Arrays;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习 ItemReader
 *
 * @author zhou
 * @date 2023/4/3
 */
@Configuration
@EnableBatchProcessing
public class ItemReaderDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public ItemReaderDemoConfig(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory) {this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job itemReaderDemoJob() {
        return jobBuilderFactory
                .get("itemReaderDemoJob")
                .start(itemReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderDemoStep() {
        // 模拟需要读取的数据
        List<String> data = Arrays.asList("one", "two", "three");

        return stepBuilderFactory
                .get("itemReaderDemoStep")
                .<String, String>chunk(2)
                // 初始化自己定义
                .reader(new ItemReaderDemoReader(data))
                // 读取数据
                .writer(list -> list.forEach(System.out::println))
                .build();
    }

}
