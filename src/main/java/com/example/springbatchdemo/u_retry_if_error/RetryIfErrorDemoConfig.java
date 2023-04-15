package com.example.springbatchdemo.u_retry_if_error;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置在发生错误后重试
 *
 * @author zhou
 * @date 2023/4/15
 */
@Configuration
@EnableBatchProcessing
public class RetryIfErrorDemoConfig {

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


    public RetryIfErrorDemoConfig(JobBuilderFactory jobBuilderFactory,
                                  StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job retryIfErrorJob() {
        return jobBuilderFactory.get("retryIfErrorJob")
                .start(retryIfErrorStep())
                .build();
    }

    @Bean
    public Step retryIfErrorStep() {

        return stepBuilderFactory.get("retryIfErrorStep")
                .<Integer, Integer>chunk(10)
                .reader(retryIfErrorReader())
                .processor(retryIfErrorProcessor())
                .writer(retryIfErrorWriter())
                // 开启错误重试模式
                // 只要开启了，不管是 reader、processor 还是 writer，只要任意步骤出错，就会开启重试模式
                .faultTolerant()
                // 在抛出哪个异常时，重试
                .retry(RuntimeException.class)
                // 重试的总次数
                // 当前这个 Step 的所有的可重试的次数，超过的话就会抛出 org.springframework.retry.RetryException
                .retryLimit(20)
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
                    throw new RuntimeException();
                }
                System.out.println("当前是第 " + count + " 次执行，成功输出");
                return item;
            }
        };
    }

    private ItemWriter<Integer> retryIfErrorWriter() {
        return list -> {
            for (Integer s : list) {
                System.out.println("成功输出：" + s);
            }
        };
    }

    private ItemReader<Integer> retryIfErrorReader() {
        List<Integer> list = new ArrayList<>();
        int times = 100;
        for (int i = 0; i < times; i++) {
            list.add(i);
        }
        return new ListItemReader<>(list);
    }
}
