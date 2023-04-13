package com.example.springbatchdemo.j_normal_file_reader;

import com.example.springbatchdemo.pojo.Customer;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * 用打印的方式模拟写入数据
 *
 * @author zhou
 * @date 2023/4/9
 */
@Component("readNormalFileItemWriter")
public class ReadNormalFileItemWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        // 写入的话，就直接模拟打印
        System.out.println("-----readNormalFileItemWriter-----");
        list.forEach(System.out::println);
        System.out.println("-----readNormalFileItemWriter-----");
    }
}
