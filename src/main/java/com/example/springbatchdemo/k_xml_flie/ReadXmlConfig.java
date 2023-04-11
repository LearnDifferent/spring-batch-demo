package com.example.springbatchdemo.k_xml_flie;

import com.example.springbatchdemo.pojo.Customer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * 学习读取 XML 文件
 *
 * @author zhou
 * @date 2023/4/11
 */
@Configuration
@EnableBatchProcessing
public class ReadXmlConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public ReadXmlConfig(JobBuilderFactory jobBuilderFactory,
                         StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job readXmlJob() {
        return jobBuilderFactory
                .get("readXmlJob")
                .start(readXmlStep())
                .build();
    }

    @Bean
    public Step readXmlStep() {
        return stepBuilderFactory
                .get("readXmlStep")
                .<Customer, Customer>chunk(2)
                .reader(readXmlItemReader())
                .writer(readXmlItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<? super Customer> readXmlItemWriter() {
        return new ReadXmlItemWriter();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<? extends Customer> readXmlItemReader() {
        // XML 使用这个 reader
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        // 设置读取 XML 文件的路径
        reader.setResource(new ClassPathResource("xml_demo.xml"));
        // 设置要读取 <customer> 标签内的内容
        reader.setFragmentRootElementName("customer");

        // 设置 XML 反序列化：
        // 设置 XStreamMarshaller，用于反序列化 XML 文件
        XStreamMarshaller unMarshaller = new XStreamMarshaller();
        // 设置解析器，用于映射 XML 文件中的 <customer> 标签中的内容为 Customer 对象
        Map<String, Class> alias = new HashMap<>();
        alias.put("customer", Customer.class);
        unMarshaller.setAliases(alias);

        reader.setUnmarshaller(unMarshaller);

        return reader;
    }
}
