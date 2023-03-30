package com.example.springbatchdemo.third_split;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Split
 *
 * @author zhou
 * @date 2023/3/30
 */
@EnableBatchProcessing
@SpringBootApplication
public class SplitDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SplitDemoApplication.class, args);
    }
}
