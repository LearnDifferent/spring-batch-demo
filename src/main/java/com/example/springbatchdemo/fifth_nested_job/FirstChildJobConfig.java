package com.example.springbatchdemo.fifth_nested_job;

import com.example.springbatchdemo.constant.Constant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置第一个 Child Job
 *
 * @author zhou
 * @date 2023/3/30
 */
@Configuration
public class FirstChildJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public FirstChildJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job firstChildJob() {
        return jobBuilderFactory
                .get("firstChildJob")
                .start(firstChildJobStep())
                .build();
    }

    @Bean
    public Step firstChildJobStep() {
        return stepBuilderFactory
                .get("firstChildJobStep")
                .tasklet((stepContribution, chunkContext) -> {
                    // 获取 Step 的名称
                    String name = chunkContext.getStepContext().getStepName();
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
