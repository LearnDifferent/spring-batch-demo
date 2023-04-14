package com.example.springbatchdemo.q_multi_resources_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * 配置多个 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
public class MultiItemWriterConfig {

    @Bean("multiItemWriter")
    public ItemWriter<Customer> multiItemWriter() throws Exception {
        // 写入多个 ItemWriter
        CompositeItemWriter<Customer> writer = new CompositeItemWriter<>();

        // 设置代理，也就是需要执行的 ItemWriter
        List<ItemWriter<? super Customer>> delegates =
                Arrays.asList(multiXmlFileItemWriter(), multiNormalFileWriterItemWriter());
        writer.setDelegates(delegates);

        writer.afterPropertiesSet();
        return writer;
    }

    /**
     * 详见 {@link com.example.springbatchdemo.p_xml_flie_writer.XmlFileItemWriterConfig}
     *
     * @return multiXmlFileItemWriter
     * @throws Exception Exception
     */
    private ItemWriter<Customer> multiXmlFileItemWriter() throws Exception {

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
                + "multi_write_xml_file" + System.currentTimeMillis() + ".xml");
        writer.setResource(new FileSystemResource(file));
        writer.afterPropertiesSet();

        System.out.println("已输出为 XML 文件");

        return writer;
    }

    /**
     * 详见 {@link com.example.springbatchdemo.o_normal_file_writer.NormalFileWriterItemWriterConfig}
     * @return multiNormalFileWriterItemWriter
     * @throws Exception Exception
     */
    private ItemWriter<Customer> multiNormalFileWriterItemWriter() throws Exception {
        // 使用 FlatFileItemWriter 来写入普通文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        // 设置写入的位置
        File file = new File("." + File.separator
                + "multi_write_normal_file" + System.currentTimeMillis() + ".txt");

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
