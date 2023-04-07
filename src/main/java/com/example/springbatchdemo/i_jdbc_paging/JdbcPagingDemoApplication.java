package com.example.springbatchdemo.i_jdbc_paging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 学习用 JDBC 的方式读取数据库的内容
 *
 * @author zhou
 * @date 2023/4/3
 */
@SpringBootApplication
public class JdbcPagingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdbcPagingDemoApplication.class, args);
    }
}
