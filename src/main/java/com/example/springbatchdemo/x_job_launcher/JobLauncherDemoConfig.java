package com.example.springbatchdemo.x_job_launcher;

import java.util.Map;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置通过 JobLauncher 启动的 Job
 * 这里将配置类通过实现 {@link StepExecutionListener} 来作为监听器
 *
 * @author zhou
 * @date 2023/4/17
 */
@Configuration
@EnableBatchProcessing
public class JobLauncherDemoConfig implements StepExecutionListener {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JobParameters
     */
    private Map<String, JobParameter> parameters;

    public JobLauncherDemoConfig(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean("jobLauncherDemoJob")
    public Job jobLauncherDemoJob() {
        return jobBuilderFactory
                .get("jobLauncherDemoJob")
                .start(jobLauncherDemoStep())
                .build();
    }

    @Bean
    public Step jobLauncherDemoStep() {
        return stepBuilderFactory
                .get("jobLauncherDemoStep")
                // 将这个类注册为监听器
                .listener(this)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("当前 Step 包含的 JobParameters 如下：");
                    parameters.forEach((k, v) -> {
                        System.out.println("key = " + k + ", JobParameter 的 value = " + v.getValue());
                    });
                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
