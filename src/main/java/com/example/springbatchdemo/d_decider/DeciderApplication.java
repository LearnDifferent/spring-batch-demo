package com.example.springbatchdemo.d_decider;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * JobExecutionDecider
 *
 * @author zhou
 * @date 2023/3/30
 */
@SpringBootApplication
@EnableBatchProcessing
public class DeciderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeciderApplication.class, args);
    }
}
