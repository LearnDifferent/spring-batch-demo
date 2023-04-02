package com.example.springbatchdemo.d_decider;

import com.example.springbatchdemo.constant.Constant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习 JobExecutionDecider
 *
 * @author zhou
 * @date 2023/3/29
 */
@Configuration
public class DeciderDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public DeciderDemoConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public JobExecutionDecider customDecider() {
        // 先创建一个自定义的 JobExecutionDecider 实例
        return new CustomDecider();
    }

    @Bean
    public Job deciderDemoJob() {
        return jobBuilderFactory
                .get("deciderDemoJob")
                // 先通过 start() 启动一个 Step
                .start(deciderDemoStep1())
                // 再运行自定义的 Decider
                .next(customDecider())
                // 判断自定义的 JobExecutionDecider 的状态
                // 如果是 even，就走 even 的 step
                .from(customDecider()).on("EVEN").to(deciderDemoStepEven())
                // 如果是 odd，就走 odd 的 step
                .from(customDecider()).on("ODD").to(deciderDemoStepOdd())
                // 如果走了 odd 的 step，那么不管返回值是什么（这里的 on() 的参数是 pattern，也就是正则表达式）
                // ，都要再走一次 JobExecutionDecider。也就是说，如果只有走的是 even 的时候，才会停下来
                .from(deciderDemoStepOdd()).on("*").to(customDecider())
                .end().build();

    }

    @Bean
    public Step deciderDemoStep1() {
        String name = "deciderDemoStep1";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step deciderDemoStepOdd() {
        String name = "deciderDemoStepOdd";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step deciderDemoStepEven() {
        String name = "deciderDemoStepEven";
        return stepBuilderFactory
                .get(name)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println(name + Constant.IS_RUNNING);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
