package com.example.springbatchdemo.s_item_processer;

import com.example.springbatchdemo.pojo.Customer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习 ItemProcessor
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
@EnableBatchProcessing
public class ItemProcessorDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public ItemProcessorDemoConfig(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job itemProcessorDemoJob() {
        return jobBuilderFactory
                .get("itemProcessorDemoJob")
                .start(itemProcessorDemoStep())
                .build();
    }

    @Bean
    public Step itemProcessorDemoStep() {
        return stepBuilderFactory
                .get("itemProcessorDemoStep")
                .<Customer, String>chunk(20)
                .reader(itemProcessorDemoReader())
                .processor(itemProcessorDemoProcessor())
                .writer(itemProcessorDemoWriter())
                .build();
    }

    /**
     * 这里演示使用一个 ItemProcessor，
     * 来将 {@link Customer} 的 {@link Customer#getName()} 转换为大写，并将 {@link Customer} 转换为 String。
     * 如果需要，也可以使用 {@link CompositeItemProcessor} 来启用多个 ItemProcessor
     *
     * @return ItemProcessor
     */
    @Bean
    public ItemProcessor<Customer, String> itemProcessorDemoProcessor() {
        return new ItemProcessor<Customer, String>() {
            @Override
            public String process(Customer customer) throws Exception {
                customer.setName(customer.getName().toUpperCase());
                return customer.toString();
            }
        };
    }

    @Bean
    public ItemWriter<String> itemProcessorDemoWriter() {
        // 模拟 ItemWriter：直接打印出来
        return new ItemWriter<>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                for (String item : items) {
                    System.out.println(item);
                }
            }
        };
    }

    @Bean
    public ItemReader<Customer> itemProcessorDemoReader() {
        List<Customer> list = new ArrayList<>();
        long times = 50L;
        for (long i = 0L; i < times; i++) {
            list.add(new Customer(i, "name" + i, "address" + i));
        }
        return new ListItemReader<>(list);
    }

}
