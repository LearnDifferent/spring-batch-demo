package com.example.springbatchdemo.x_job_launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过发送请求，来调用 JobLauncher 启动一个 Job
 *
 * @author zhou
 * @date 2023/4/17
 */
@RestController
public class JobLauncherDemoController {

    /**
     * 引入一个 JobLauncher
     */
    private final JobLauncher jobLauncher;

    /**
     * 在配置类中配置中已经配置的 Job
     */
    private final Job jobLauncherDemoJob;

    public JobLauncherDemoController(JobLauncher jobLauncher, Job jobLauncherDemoJob) {
        this.jobLauncher = jobLauncher;
        this.jobLauncherDemoJob = jobLauncherDemoJob;
    }

    @GetMapping("/job")
    public String launchJob(@RequestParam("jobParam") String jobParam) {
        // 构建 JobParameters
        JobParameters param = new JobParametersBuilder()
                .addString("jobParam", jobParam)
                .toJobParameters();

        try {
            // 使用 JobLauncher 启动 Job
            JobExecution execution = jobLauncher.run(jobLauncherDemoJob, param);
            // 获取 JobId 并返回成功消息
            Long jobId = execution.getJobId();
            return jobId + "已经启动成功";
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
            return "启动 Job 失败";
        }
    }
}
