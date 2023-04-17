package com.example.springbatchdemo.v_skip_if_error;

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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置在发生错误后跳过
 *
 * @author zhou
 * @date 2023/4/15
 */
@Configuration
@EnableBatchProcessing
public class SkipIfErrorDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * 当前执行的次数
     */
    private int count = 0;

    /**
     * 在这 2 个执行次数之间的偶数次，都要抛出异常
     */
    private final int FROM_TIMES = 30;
    private final int TO_TIMES = 40;


    public SkipIfErrorDemoConfig(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job skipIfErrorJob() {
        return jobBuilderFactory.get("skipIfErrorJob")
                .start(skipIfErrorStep())
                .build();
    }

    @Bean
    public Step skipIfErrorStep() {

        return stepBuilderFactory.get("skipIfErrorStep")
                .<Integer, Integer>chunk(10)
                .reader(retryIfErrorReader())
                .processor(retryIfErrorProcessor())
                .writer(retryIfErrorWriter())
                .faultTolerant()
                // 在抛出哪个异常时，跳过
                .skip(RuntimeException.class)
                // 跳过的总次数
                .skipLimit(20)
                .build();

    }

    private ItemProcessor<? super Integer, Integer> retryIfErrorProcessor() {
        return new ItemProcessor<Integer, Integer>() {
            @Override
            public Integer process(Integer item) throws Exception {
                // 执行次数+1
                count++;
                if (count % 2 == 0 && count >= FROM_TIMES && count <= TO_TIMES) {
                    System.err.println("在 " + FROM_TIMES + " 和 " + TO_TIMES + " 之间的偶数次会故意报错");
                    System.err.println("当前是第 " + count + " 次执行，抛出异常");
                    // 注意，这里是跳过，也就是说程序不会暂停
                    throw new RuntimeException();
                }
                // 如果前面报错而跳过了，下面这个就不会打印出来
                System.out.println("当前是第 " + count + " 次执行，成功输出");
                return item;
            }
        };
    }

    private ItemWriter<Integer> retryIfErrorWriter() {
        // 什么都不做
        return new ListItemWriter<>();
    }

    private ItemReader<Integer> retryIfErrorReader() {
        List<Integer> list = new ArrayList<>();
        int times = 60;
        for (int i = 0; i < times; i++) {
            list.add(i);
        }
        return new ListItemReader<>(list);
    }
}
