package com.example.springbatchdemo.second_job_flow;

import com.example.springbatchdemo.constant.Constant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习 Job Flow 通过 on(), to(), from() 和 end() 来指定 Step 运行的顺序
 * 注：实际上这里是 Job 可以通过 on(), to(), from() 和 end() 来指定 Step 运行的顺序
 *
 * @author zhou
 * @date 2023/3/29
 */
@Configuration
public class JobFlowOnToFromEndConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    public JobFlowOnToFromEndConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jobOnToFromEndFlowDemo() {
        return jobBuilderFactory
                .get("jobOnToFromEndFlowDemo")
                .start(firstOnToFromEndFlowStep()).on("COMPLETED").to(secondOnToFromEndFlowStep())
                .from(secondOnToFromEndFlowStep()).on("COMPLETED").to(thirdOnToFromEndFlowStep())
                // 还可以加上 fail()，这样的话，后面的 Step 就不会执行（还有其他的方法）
                .from(thirdOnToFromEndFlowStep()).on("COMPLETED").to(lastOnToFromEndFlowStep())
                .from(lastOnToFromEndFlowStep()).end()
                .build();
    }

    @Bean
    public Step lastOnToFromEndFlowStep() {
        String name = "lastOnToFromEndFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step thirdOnToFromEndFlowStep() {
        String name = "thirdOnToFromEndFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step secondOnToFromEndFlowStep() {
        String name = "secondOnToFromEndFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step firstOnToFromEndFlowStep() {
        String name = "firstOnToFromEndFlowStep";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
