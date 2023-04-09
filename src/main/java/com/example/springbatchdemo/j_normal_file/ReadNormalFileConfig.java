package com.example.springbatchdemo.j_normal_file;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 学习从普通文件中读取数据
 *
 * @author zhou
 * @date 2023/4/9
 */
@Configuration
@EnableBatchProcessing
public class ReadNormalFileConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ReadNormalFileItemWriter readNormalFileItemWriter;

    public ReadNormalFileConfig(JobBuilderFactory jobBuilderFactory,
                                StepBuilderFactory stepBuilderFactory,
                                @Qualifier("readNormalFileItemWriter")
                                        ReadNormalFileItemWriter readNormalFileItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.readNormalFileItemWriter = readNormalFileItemWriter;
    }

    @Bean
    public Job readNormalFileJob() {
        return jobBuilderFactory
                .get("readNormalFileJob")
                .start(readNormalFileStep())
                .build();
    }

    @Bean
    public Step readNormalFileStep() {
        return stepBuilderFactory
                .get("readNormalFileStep")
                .<Customer, Customer>chunk(2)
                .reader(readNormalFileItemReader())
                .writer(readNormalFileItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<? extends Customer> readNormalFileItemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        // 设置读取文件的路径
        reader.setResource(new ClassPathResource("normal_file.txt"));
        // 跳过第一行，因为第一行是表头
        reader.setLinesToSkip(1);
        // 设置解析器：
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        // 设置每行中，每个分隔后的字段的含义
        tokenizer.setNames("id", "name", "address");
        // 设置行映射
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        // 放入刚刚的解析器
        mapper.setLineTokenizer(tokenizer);
        // 设置映射
        mapper.setFieldSetMapper(fieldSet -> {
            Customer customer = new Customer();
            customer.setId(fieldSet.readLong("id"));
            customer.setName(fieldSet.readString("name"));
            customer.setAddress(fieldSet.readString("address"));
            return customer;
        });
        // 检验映射
        mapper.afterPropertiesSet();

        // 放入映射器
        reader.setLineMapper(mapper);

        return reader;
    }

}