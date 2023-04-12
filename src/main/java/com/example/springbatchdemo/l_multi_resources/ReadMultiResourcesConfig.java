package com.example.springbatchdemo.l_multi_resources;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * 学习读取多个资源
 *
 * @author zhou
 * @date 2023/4/12
 */
@Configuration
@EnableBatchProcessing
public class ReadMultiResourcesConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ReadMultiResourcesItemWriter readMultiResourcesItemWriter;

    /**
     * 读取所有 multi_normal_file 开头，以 .txt 结尾的文件
     */
    @Value("classpath:multi_normal_file*.txt")
    private Resource[] files;

    public ReadMultiResourcesConfig(JobBuilderFactory jobBuilderFactory,
                                    StepBuilderFactory stepBuilderFactory,
                                    @Qualifier("readMultiResourcesItemWriter")
                                            ReadMultiResourcesItemWriter readMultiResourcesItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.readMultiResourcesItemWriter = readMultiResourcesItemWriter;
    }

    @Bean
    public Job readMultiResourcesJob() {
        return jobBuilderFactory
                .get("readMultiResourcesJob")
                .start(readMultiResourcesStep())
                .build();
    }

    public Step readMultiResourcesStep() {
        return stepBuilderFactory
                .get("readMultiResourcesStep")
                .<Customer, Customer>chunk(2)
                .reader(readMultiResourcesItemReader())
                .writer(readMultiResourcesItemWriter)
                .build();
    }

    @Bean
    public MultiResourceItemReader<? extends Customer> readMultiResourcesItemReader() {
        MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();

        // 设置委托（用于指定读取单个文件时的方法）
        reader.setDelegate(readMultiResourcesItemReaderDelegate());
        // 设置数据源
        reader.setResources(files);

        return reader;
    }

    @Bean
    public ResourceAwareItemReaderItemStream<? extends Customer> readMultiResourcesItemReaderDelegate() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

        // 这里就不用设置文件路径，因为这里只是指定单个文件的读取的方式：reader.setResource();
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
