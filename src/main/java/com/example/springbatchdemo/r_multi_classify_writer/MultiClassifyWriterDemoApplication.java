package com.example.springbatchdemo.r_multi_classify_writer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习根据不同分类来选择 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@SpringBootApplication
public class MultiClassifyWriterDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiClassifyWriterDemoApplication.class, args);
    }
}
