package com.example.springbatchdemo.n_jdbc_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 学习使用 JDBC 的方式写入数据库
 *
 * @author zhou
 * @date 2023/4/13
 */
@Configuration
@EnableBatchProcessing
public class JdbcWriterDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemWriter<Customer> jdbcWriterDemoItemWriter;


    public JdbcWriterDemoConfig(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory,
                                @Qualifier("jdbcWriterDemoItemWriter")
                                        ItemWriter<Customer> jdbcWriterDemoItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jdbcWriterDemoItemWriter = jdbcWriterDemoItemWriter;
    }

    @Bean
    public Job jdbcWriterDemoJob() {
        return jobBuilderFactory
                .get("jdbcWriterDemoJob")
                .start(jdbcWriterDemoStep())
                .build();
    }

    @Bean
    public Step jdbcWriterDemoStep() {
        return stepBuilderFactory
                .get("jdbcWriterDemoStep")
                .<Customer, Customer>chunk(20)
                .reader(jdbcWriterDemoItemReader())
                .writer(jdbcWriterDemoItemWriter)
                .build();
    }

    /**
     * 模拟读取 Customer 数据
     *
     * @return {@link ListItemReader}
     */
    @Bean
    public ListItemReader<? extends Customer> jdbcWriterDemoItemReader() {
        List<Customer> list = new ArrayList<>();
        long times = 100L;
        for (long i = 0; i < times; i++) {
            Customer customer = new Customer();
            customer.setId(i);
            customer.setName("name" + i);
            customer.setAddress("address" + i);
            list.add(customer);
        }

        return new ListItemReader<>(list);
    }

}
