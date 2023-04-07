package com.example.springbatchdemo.i_jdbc_paging;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * 设置 Writer
 *
 * @author zhou
 * @date 2023/4/7
 */
@Component("jdbcPagingDemoWriter ")
public class JdbcPagingDemoWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        // 写入的话，就直接模拟打印
        System.out.println("-----jdbcPagingDemoWriter-----");
        list.forEach(System.out::println);
        System.out.println("-----jdbcPagingDemoWriter-----");
    }
}
