package com.example.springbatchdemo.v_skip_if_error;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 在发生错误后跳过
 *
 * @author zhou
 * @date 2023/4/17
 */
@SpringBootApplication
public class SkipIfErrorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkipIfErrorApplication.class, args);
    }
}
