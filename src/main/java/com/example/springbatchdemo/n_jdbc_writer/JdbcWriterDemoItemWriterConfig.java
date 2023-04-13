package com.example.springbatchdemo.n_jdbc_writer;

import com.example.springbatchdemo.pojo.Customer;
import javax.sql.DataSource;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置一个用 JDBC 的方式写入数据库的 ItemWriter
 *
 * @author zhou
 * @date 2023/4/13
 */
@Configuration
public class JdbcWriterDemoItemWriterConfig {

    private final DataSource dataSource;

    public JdbcWriterDemoItemWriterConfig(DataSource dataSource) {this.dataSource = dataSource;}

    @Bean("jdbcWriterDemoItemWriter")
    public ItemWriter<Customer> jdbcWriterDemoItemWriter() {
        // JdbcBatchItemWriter 是一个用 JDBC 的方式读取数据的 ItemWriter
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("insert into customer_empty (id, name, address) values (:id, :name, :address);");
        // 设置占位符的值为 Customer 的所有属性值
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return writer;
    }

}
