package com.example.springbatchdemo.b_job_flow;

import com.example.springbatchdemo.constant.Constant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习通过 Flow Builder 来创建 Job Flow
 *
 * @author zhou
 * @date 2023/3/29
 */
@Configuration
public class JobFlowBuilderConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public JobFlowBuilderConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jobWithFlow() {
        String name = "jobWithFlow";
        return jobBuilderFactory
                .get(name)
                // 使用 Flow 而不是 Step
                .start(jobFlowDemo1())
                .next(stepExceptionFlowStep())
                .end()
                .build();
    }

    /**
     * Flow 可以由多个 Step 组成
     *
     * @return 返回 Flow
     */
    @Bean
    public Flow jobFlowDemo1() {
        String name = "jobFlowDemo1";
        return new FlowBuilder<Flow>(name)
                .start(flowStep1())
                .next(flowStep2())
                .build();
    }

    @Bean
    public Step flowStep1() {
        String name = "flowStep1";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING + "：这是 Flow 里面的 Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step flowStep2() {
        String name = "flowStep2";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING + "：这是 Flow 里面的 Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step stepExceptionFlowStep() {
        String name = "stepExceptionFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING + "：这不是 Flow 里面的 Step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
