package com.example.springbatchdemo.fifth_nested_job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Nested Job
 *
 * @author zhou
 * @date 2023/3/30
 */
@SpringBootApplication
@EnableBatchProcessing
public class NestedJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(NestedJobApplication.class, args);
    }
}
