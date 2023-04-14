package com.example.springbatchdemo.o_normal_file_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.io.File;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * 配置写入文件的 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
public class NormalFileWriterItemWriterConfig {

    @Bean("normalFileWriterItemWriter")
    public ItemWriter<Customer> normalFileWriterItemWriter() throws Exception {
        // 使用 FlatFileItemWriter 来写入普通文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        // 设置写入的位置
        File file = new File("." + File.separator
                + "write_normal_file" + System.currentTimeMillis() + ".txt");

        // 这里不用写 file.createNewFile()
        // 因为写入文件的操作是下面的代码来完成的
        FileSystemResource resource = new FileSystemResource(file);
        writer.setResource(resource);
        System.out.println("文件写入于：" + resource.getPath());

        // 设置序列化，也就是将实体类映射到文件的格式
        writer.setLineAggregator(new LineAggregator<Customer>() {
            @Override
            public String aggregate(Customer customer) {
                return customer.getId() + "," + customer.getName()
                        + "," + customer.getAddress();
            }
        });

        writer.afterPropertiesSet();
        return writer;
    }
}
