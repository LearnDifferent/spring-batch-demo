package com.example.springbatchdemo.sixth_listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * 通过实现接口的方式实现一个 Job Listener
 *
 * @author zhou
 * @date 2023/3/31
 */
public class CustomJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        System.out.println("Job Listener - Before: " + jobName);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        System.out.println("Job Listener - After: " + jobName);
    }
}