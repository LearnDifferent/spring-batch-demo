package com.example.springbatchdemo.f_listener;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Listener
 *
 * @author zhou
 * @date 2023/3/31
 */
@SpringBootApplication
@EnableBatchProcessing
public class ListenerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListenerDemoApplication.class, args);
    }
}
