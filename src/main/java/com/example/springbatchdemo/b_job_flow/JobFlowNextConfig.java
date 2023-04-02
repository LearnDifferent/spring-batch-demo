package com.example.springbatchdemo.b_job_flow;

import com.example.springbatchdemo.constant.Constant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习 Job Flow 通过 next() 来指定 Step 运行的顺序
 * 注：实际上这里是 Job 可以通过 next() 来指定 Step 运行的顺序
 *
 * @author zhou
 * @date 2023/3/28
 */
@Configuration
public class JobFlowNextConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public JobFlowNextConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jobNextFlowDemo() {
        return jobBuilderFactory
                .get("jobNextFlowDemo")
                .start(firstNextFlowStep())
                .next(secondNextFlowStep())
                .next(thirdNextFlowStep())
                .next(lastNextFlowStep())
                .build();
    }

    @Bean
    public Step lastNextFlowStep() {
        String name = "lastNextFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step thirdNextFlowStep() {
        String name = "thirdNextFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step secondNextFlowStep() {
        String name = "secondNextFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step firstNextFlowStep() {
        String name = "firstNextFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
