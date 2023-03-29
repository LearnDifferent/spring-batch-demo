package com.example.springbatchdemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用 @EnableBatchProcessing 来开启 Spring Batch
 */
@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchDemoApplication.class, args);
    }

}
