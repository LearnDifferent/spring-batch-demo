package com.example.springbatchdemo.g_job_parameters;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 启动的时候获取 Parameter 参数（Program arguments）
 *
 * @author zhou
 * @date 2023/4/3
 */
@Configuration
@EnableBatchProcessing
public class JobParametersDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public JobParametersDemoConfig(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jobParametersDemoJob() {
        return jobBuilderFactory
                .get("jobParametersDemoJob")
                .start(jobParametersDemoStep())
                .build();
    }

    @Bean
    public Step jobParametersDemoStep() {
        return stepBuilderFactory
                .get("jobParametersDemoStep")
                .listener(new JobParametersDemoListener())
                .tasklet(((stepContribution, chunkContext) -> RepeatStatus.FINISHED))
                .build();
    }
}