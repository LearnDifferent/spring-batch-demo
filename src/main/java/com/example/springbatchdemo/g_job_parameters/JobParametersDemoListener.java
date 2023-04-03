package com.example.springbatchdemo.g_job_parameters;

import java.util.Map;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * JobParameters Demo 的 Listener
 * 用于打印启动时候的 Parameters 参数
 *
 * @author zhou
 * @date 2023/4/3
 */
public class JobParametersDemoListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // 演示获取启动参数中的 Parameters
        JobParameters jobParameters = stepExecution.getJobParameters();
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        parameters.forEach((k, v) -> System.out.println("key: " + k + " ; value: " + v));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
