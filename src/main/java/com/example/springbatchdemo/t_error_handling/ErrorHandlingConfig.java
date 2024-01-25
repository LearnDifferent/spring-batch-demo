package com.example.springbatchdemo.t_error_handling;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 处理异常
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
@EnableBatchProcessing
public class ErrorHandlingConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    public ErrorHandlingConfig(JobBuilderFactory jobBuilderFactory,
                               StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job errorHandlingJob() {
        return jobBuilderFactory.get("errorHandlingJob")
                .start(errorHandlingStep())
                .build();
    }

    @Bean
    public Step errorHandlingStep() {
        return stepBuilderFactory.get("errorHandlingStep")
                .tasklet(errorHandlingTasklet())
                .build();
    }

    private Tasklet errorHandlingTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                // 先获取 ExecutionContext 的参数
                ExecutionContext executionContext =
                        chunkContext.getStepContext().getStepExecution().getExecutionContext();
                // 是否包含 key 为 "success" 的参数
                Boolean success = (Boolean) executionContext.get("success");
                if (success != null && success) {
                    System.out.println("第二次执行就会成功");
                    // 返回完成
                    return RepeatStatus.FINISHED;
                } else {
                    // 因为下一次执行会成功，所以这里手动将 success 设置为 true
                    executionContext.put("success", true);
                    // 手动抛出异常
                    throw new RuntimeException("第一次执行失败");
                }
            }
        };
    }


}
