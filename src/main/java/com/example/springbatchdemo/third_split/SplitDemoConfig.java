package com.example.springbatchdemo.third_split;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 学习 Split
 *
 * @author zhou
 * @date 2023/3/29
 */
@Configuration
public class SplitDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public SplitDemoConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job jobSplitDemo() {
        return jobBuilderFactory
                .get("jobSplitDemo")
                // 开始的时候，要传入一个 FLow
                // 这个 start() 里面的 FLow 也是被 Split 管控的，也就是会放入线程池中执行
                // 所以即便是在 start() 里面，也不保证会第一个执行
                .start(flowInSplitDemo0())
                // split 的时候，传入一个简单的异步线程池
                .split(new SimpleAsyncTaskExecutor())
                // 开始 Split 之后，可以传入多个 FLow
                .add(flowInSplitDemo1(), flowInSplitDemo2(), flowInSplitDemo2())
                .end().build();
    }

    /**
     * 创建一个 Flow
     * 用于验证在有 Split 的情况下，即便这个 FLow 是在 start() 里面，也不一定是第一个执行的
     *
     * @return 返回一个 Flow
     */
    @Bean
    public Flow flowInSplitDemo0() {
        return new FlowBuilder<Flow>("flowInSplitDemo0")
                .start(stepInSplitDemo0())
                .build();
    }

    /**
     * 创建第一个 split 里面的 Flow
     *
     * @return 返回一个 Flow
     */
    @Bean
    public Flow flowInSplitDemo1() {
        return new FlowBuilder<Flow>("flowInSplitDemo1")
                .start(stepInSplitDemo1())
                .next(stepInSplitDemo2())
                .build();
    }

    /**
     * 创建第二个 split 里面的 Flow，其中的 Step 和第一个 Flow 相同，但是会被 Split 放入另外一个线程
     *
     * @return 返回一个 Flow
     */
    @Bean
    public Flow flowInSplitDemo2() {
        return new FlowBuilder<Flow>("flowInSplitDemo2")
                .start(stepInSplitDemo1())
                .next(stepInSplitDemo2())
                .build();
    }

    /**
     * 同理，创建第三个 split 里面的 FLow
     *
     * @return 返回一个 Flow
     */
    @Bean
    public Flow flowInSplitDemo3() {
        return new FlowBuilder<Flow>("flowInSplitDemo3")
                .start(stepInSplitDemo1())
                .next(stepInSplitDemo2())
                .build();
    }

    /**
     * 这个 Step 是专门为 flowInSplitDemo0 准备的，不会复用。
     * 其他的 Step 则是会复用。
     *
     * @return 返回一个 Step
     */
    @Bean
    public Step stepInSplitDemo0() {
        String name = "stepInSplitDemo0";
        return stepBuilderFactory
                .get(name)
                .tasklet(printCurrent())
                .build();
    }

    @Bean
    public Step stepInSplitDemo1() {
        String name = "stepInSplitDemo1";
        return stepBuilderFactory
                .get(name)
                .tasklet(printCurrent())
                .build();
    }

    @Bean
    public Step stepInSplitDemo2() {
        String name = "stepInSplitDemo2";
        return stepBuilderFactory
                .get(name)
                .tasklet(printCurrent())
                .build();
    }

    /**
     * 返回一个用于打印当前信息的 Tasklet
     *
     * @return 返回一个用于打印当前信息的 Tasklet
     */
    private Tasklet printCurrent() {
        // new Tasklet() :
        return (stepContribution, chunkContext) -> {
            String msg = chunkContext.getStepContext().getStepName()
                    + " has been executed on thread "
                    + Thread.currentThread().getName();
            System.out.println(msg);
            return RepeatStatus.FINISHED;
        };
    }
}
