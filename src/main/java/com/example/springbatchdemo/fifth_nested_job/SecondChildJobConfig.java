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
 * 配置第 2 个 Child Job
 *
 * @author zhou
 * @date 2023/3/30
 */
@Configuration
public class SecondChildJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public SecondChildJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job secondChildJob() {
        return jobBuilderFactory
                .get("secondChildJob")
                .start(secondChildJobStep())
                .build();
    }

    @Bean
    public Step secondChildJobStep() {
        return stepBuilderFactory
                .get("secondChildJobStep")
                .tasklet((stepContribution, chunkContext) -> {
                    String name = stepContribution.getStepExecution().getStepName();
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}