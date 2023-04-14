package com.example.springbatchdemo.p_xml_flie_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 读取 XML 文件的 ItemWriter 的配置
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
@EnableBatchProcessing
public class XmlFileWriterConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemWriter<Customer> xmlFileItemWriter;

    public XmlFileWriterConfig(JobBuilderFactory jobBuilderFactory,
                               StepBuilderFactory stepBuilderFactory,
                               @Qualifier("xmlFileItemWriter") ItemWriter<Customer> xmlFileItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.xmlFileItemWriter = xmlFileItemWriter;
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
                .reader(xmlFileItemReader())
                .writer(xmlFileItemWriter)
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> xmlFileItemReader() {
        List<Customer> list = new ArrayList<>();
        long times = 50L;
        for (long i = 0L; i < times; i++) {
            list.add(new Customer(i, "name" + i, "address" + i));
        }
        return new ListItemReader<>(list);
    }
}
