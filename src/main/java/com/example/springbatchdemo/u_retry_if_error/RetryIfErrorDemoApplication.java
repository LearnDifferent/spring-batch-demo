package com.example.springbatchdemo.u_retry_if_error;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习在发生错误后重试
 *
 * @author zhou
 * @date 2023/4/15
 */
@SpringBootApplication
public class RetryIfErrorDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetryIfErrorDemoApplication.class, args);
    }
}
