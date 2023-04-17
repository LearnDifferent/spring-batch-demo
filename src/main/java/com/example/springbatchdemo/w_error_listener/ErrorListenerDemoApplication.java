package com.example.springbatchdemo.w_error_listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习发生错误时的监听器
 *
 * @author zhou
 * @date 2023/4/17
 */
@SpringBootApplication
public class ErrorListenerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ErrorListenerDemoApplication.class, args);
    }
}
