package com.example.springbatchdemo.i_jdbc_paging_reader;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.springbatchdemo.pojo.Customer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习用 JDBC 的方式读取数据库的内容
 *
 * @author zhou
 * @date 2023/4/3
 */
@Configuration
@EnableBatchProcessing
public class JdbcPagingDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    /**
     * 数据源
     */
    private final DruidDataSource dataSource;
    /**
     * 自定义的 ItemWriter
     */
    private final ItemWriter<? super Customer> itemWriter;


    public JdbcPagingDemoConfig(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory,
                                DruidDataSource dataSource,
                                @Qualifier("jdbcPagingDemoWriter ") ItemWriter<? super Customer> itemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.itemWriter = itemWriter;
    }

    @Bean
    public Job jdbcPagingDemoJob() {
        return jobBuilderFactory
                .get("jdbcPagingDemoJob")
                .start(jdbcPagingDemoStep())
                .build();
    }

    @Bean
    public Step jdbcPagingDemoStep() {
        return stepBuilderFactory
                .get("jdbcPagingDemoStep")
                .<Customer, Customer>chunk(2)
                .reader(jdbcPagingItemReader())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Customer> jdbcPagingItemReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
        // 设置数据源
        reader.setDataSource(dataSource);
        // 设置每次读取的大小
        reader.setFetchSize(2);
        // 设置映射关系
        reader.setRowMapper((rs, rowNum) -> {
            // 从结果集中读取每列的数据，然后映射到 Customer 对象中
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setName(rs.getString("name"));
            customer.setAddress(rs.getString("address"));
            return customer;
        });

        // 设置 MySQL 查询
        MySqlPagingQueryProvider query = new MySqlPagingQueryProvider();
        query.setSelectClause("id, name, address");
        query.setFromClause("customer_batch_demo");
        // 设置 MySQL 的排序
        Map<String, Order> sortBy = new HashMap<>(1);
        sortBy.put("id", Order.DESCENDING);
        query.setSortKeys(sortBy);

        // 将设置好的 MySQL 查询 注入到 reader 对象中
        reader.setQueryProvider(query);

        // 返回 reader 对象
        return reader;
    }
}