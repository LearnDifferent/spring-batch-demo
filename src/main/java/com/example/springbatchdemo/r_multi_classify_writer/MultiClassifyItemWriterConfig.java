package com.example.springbatchdemo.r_multi_classify_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * 配置根据不同分类来选择 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
public class MultiClassifyItemWriterConfig {

    @Bean("multiClassifyItemWriter")
    public ItemWriter<Customer> multiClassifyItemWriter() throws Exception {

        // ClassifierCompositeItemWriter 可以根据不同分类来选择 ItemWriter
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<>();

        // 设置一下分类的方式，也就是在哪种情况下，使用哪个 ItemWriter
        writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
            @Override
            public ItemWriter<? super Customer> classify(Customer customer) {
                if (customer.getId() % 2 == 0) {
                    return multiClassifyXmlFileItemWriter();
                } else {
                    return multiClassifyNormalFileWriterItemWriter();
                }
            }
        });

        return writer;
    }

    @Bean("multiClassifyXmlFileItemWriter")
    public StaxEventItemWriter<Customer> multiClassifyXmlFileItemWriter() {

        // 设置映射
        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap<>();
        // 这样的话，文件中就类似：<customer><id>0</id><name>name0</name><address>address0</address>
        aliases.put("customer", Customer.class);
        marshaller.setAliases(aliases);

        // 设置 ItemWriter
        StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();
        writer.setMarshaller(marshaller);
        // 设置最外层的标签
        writer.setRootTagName("customers");

        // 设置输出路径
        File file = new File("." + File.separator
                + "multi_classify_write_xml_file" + System.currentTimeMillis() + ".xml");
        writer.setResource(new FileSystemResource(file));
        try {
            writer.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException("设置 ItemWriter 失败", e);
        }

        System.out.println("已输出为 XML 文件：" + file.getName());

        return writer;
    }

    @Bean("multiClassifyNormalFileWriterItemWriter")
    public FlatFileItemWriter<Customer> multiClassifyNormalFileWriterItemWriter() {
        // 使用 FlatFileItemWriter 来写入普通文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        // 设置写入的位置
        File file = new File("." + File.separator
                + "multi_classify_write_normal_file" + System.currentTimeMillis() + ".txt");

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

        try {
            writer.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException("设置 ItemWriter 失败", e);
        }
        return writer;
    }
}
