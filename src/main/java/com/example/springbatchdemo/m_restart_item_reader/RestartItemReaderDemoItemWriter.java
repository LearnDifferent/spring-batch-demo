package com.example.springbatchdemo.m_restart_item_reader;

import com.example.springbatchdemo.pojo.Customer;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * 模拟读取
 *
 * @author zhou
 * @date 2023/4/12
 */
@Component("restartItemReaderDemoItemWriter")
public class RestartItemReaderDemoItemWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        // 写入的话，就直接模拟打印
        System.out.println("-----restartItemReaderDemoItemWriter-----");
        list.forEach(System.out::println);
        System.out.println("-----restartItemReaderDemoItemWriter-----");
    }
}
