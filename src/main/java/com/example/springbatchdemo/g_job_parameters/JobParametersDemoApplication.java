package com.example.springbatchdemo.g_job_parameters;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 读取 Job Parameters
 * 启动的时候，如果是 IDEA，可以在 Program arguments 中设置 key=value，例如：test=abc
 *
 * @author zhou
 * @date 2023/4/3
 */
@SpringBootApplication
public class JobParametersDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobParametersDemoApplication.class, args);
    }
}
