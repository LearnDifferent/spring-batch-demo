package com.example.springbatchdemo.second_job_flow;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Job Flow
 *
 * @author zhou
 * @date 2023/3/30
 */
@SpringBootApplication
@EnableBatchProcessing
public class JobFlowDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobFlowDemoApplication.class, args);
    }
}
