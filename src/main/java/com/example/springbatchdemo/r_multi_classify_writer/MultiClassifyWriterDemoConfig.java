package com.example.springbatchdemo.r_multi_classify_writer;

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
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
@EnableBatchProcessing
public class MultiClassifyWriterDemoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemWriter<Customer> multiClassifyItemWriter;
    /**
     * 这里要指定 StaxEventItemWriter，因为 {@link #multiClassifyStep()} 里面要使用 ItemStream
     */
    private final StaxEventItemWriter<Customer> multiClassifyXmlFileItemWriter;

    /**
     * 这里要指定 FlatFileItemWriter，因为 {@link #multiClassifyStep()} 里面要使用 ItemStream
     */
    private final FlatFileItemWriter<Customer> multiClassifyNormalFileWriterItemWriter;


    public MultiClassifyWriterDemoConfig(JobBuilderFactory jobBuilderFactory,
                                         StepBuilderFactory stepBuilderFactory,
                                         @Qualifier("multiClassifyItemWriter")
                                                 ItemWriter<Customer> multiClassifyItemWriter,
                                         @Qualifier("multiClassifyXmlFileItemWriter")
                                                 StaxEventItemWriter<Customer> multiClassifyXmlFileItemWriter,
                                         @Qualifier("multiClassifyNormalFileWriterItemWriter")
                                                 FlatFileItemWriter<Customer> multiClassifyNormalFileWriterItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.multiClassifyItemWriter = multiClassifyItemWriter;
        this.multiClassifyXmlFileItemWriter = multiClassifyXmlFileItemWriter;
        this.multiClassifyNormalFileWriterItemWriter = multiClassifyNormalFileWriterItemWriter;
    }

    @Bean
    public Job multiClassifyJob() {
        return jobBuilderFactory
                .get("multiClassifyJob")
                .start(multiClassifyStep())
                .build();
    }

    @Bean
    public Step multiClassifyStep() {
        return stepBuilderFactory
                .get("multiClassifyStep")
                .<Customer, Customer>chunk(20)
                // 要先载入 Reader
                .reader(multiClassifyItemReader())
                // 然后配置 Writer
                .writer(multiClassifyItemWriter)
                // Writer 这里要开启 Stream，并传入需要的 ItemStream
                .stream(multiClassifyXmlFileItemWriter)
                .stream(multiClassifyNormalFileWriterItemWriter)
                .build();
    }

    @Bean
    public ItemReader<Customer> multiClassifyItemReader() {
        List<Customer> list = new ArrayList<>();
        long times = 50L;
        for (long i = 0L; i < times; i++) {
            list.add(new Customer(i, "name" + i, "address" + i));
        }
        return new ListItemReader<>(list);
    }
}
