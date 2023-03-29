package com.example.springbatchdemo.fourth_decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * 自定义的决策器
 *
 * @author zhou
 * @date 2023/3/29
 */
public class CustomDecider implements JobExecutionDecider {

    private int count = 0;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        count++;
        if (count % 2 == 0) {
            return new FlowExecutionStatus("EVEN");
        }
        // 可以返回自定义的状态
        return new FlowExecutionStatus("ODD");
    }
}