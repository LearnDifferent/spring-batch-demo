package com.example.springbatchdemo.x_job_launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习通过 JobLauncher 启动一个 Job
 *
 * @author zhou
 * @date 2023/4/17
 */
@SpringBootApplication
public class JobLauncherDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobLauncherDemoApplication.class, args);
    }
}
