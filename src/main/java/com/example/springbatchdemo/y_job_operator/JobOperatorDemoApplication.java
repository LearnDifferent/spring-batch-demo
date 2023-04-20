package com.example.springbatchdemo.y_job_operator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习 JobOperator
 * 可以在运行的时候加上 stop-auto 环境（application-stop-auto.yml）
 * ，这样就不会启动程序的时候就启动 Job
 *
 * @author zhou
 * @date 2023/4/20
 */
@SpringBootApplication
public class JobOperatorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobOperatorDemoApplication.class, args);
    }
}