package com.example.springbatchdemo.p_xml_flie_writer;

import com.example.springbatchdemo.pojo.Customer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * 配置读取 XML 的 ItemWriter
 *
 * @author zhou
 * @date 2023/4/14
 */
@Configuration
public class XmlFileItemWriterConfig {

    @Bean("xmlFileItemWriter")
    public ItemWriter<Customer> xmlFileItemWriter() throws Exception {

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
                + "write_xml_file" + System.currentTimeMillis() + ".xml");
        writer.setResource(new FileSystemResource(file));
        writer.afterPropertiesSet();

        System.out.println("已输出为 XML 文件");

        return writer;
    }
}
