package com.example.springbatchdemo.f_listener;

import java.util.Arrays;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习 Listener
 *
 * @author zhou
 * @date 2023/3/31
 */
@Configuration
public class ListenerJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public ListenerJobConfig(JobBuilderFactory jobBuilderFactory,
                             StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job listenerJob() {
        return jobBuilderFactory
                .get("listenerJob")
                .start(listerStep())
                // 使用 Listener
                .listener(new CustomJobListener())
                .build();
    }

    @Bean
    public Step listerStep() {
        return stepBuilderFactory
                .get("listerStep")
                // 输入 String，输出 String，每 2 个 item 为一组
                .<String , String>chunk(2)
                // 允许失败
                .faultTolerant()
                // 使用 Listener
                .listener(new CustomChunkListener())
                .reader(readerInListenerDemo())
                .writer(writerInListenerDemo())
                .build();
    }

    @Bean
    public ItemReader<String> readerInListenerDemo() {
        // 模拟读取数据，返回一个 ListItemReader
        return new ListItemReader<>(Arrays.asList("one", "two", "three"));
    }

    @Bean
    public ItemWriter<? super String> writerInListenerDemo() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                // 直接打印输入的 List 的内容
                list.forEach(System.out::println);
            }
        };
    }

}
