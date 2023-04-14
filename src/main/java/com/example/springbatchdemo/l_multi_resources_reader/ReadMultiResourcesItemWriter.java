package com.example.springbatchdemo.l_multi_resources_reader;

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
@Component("readMultiResourcesItemWriter")
public class ReadMultiResourcesItemWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        // 写入的话，就直接模拟打印
        System.out.println("-----readMultiResourcesItemWriter-----");
        list.forEach(System.out::println);
        System.out.println("-----readMultiResourcesItemWriter-----");
    }
}
