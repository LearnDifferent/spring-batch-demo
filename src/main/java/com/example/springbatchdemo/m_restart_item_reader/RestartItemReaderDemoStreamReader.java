package com.example.springbatchdemo.m_restart_item_reader;

import com.example.springbatchdemo.pojo.Customer;
import java.util.Objects;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * 这里实现 {@link ItemStreamReader} 接口，该接口 {@code extends ItemStream, ItemReader<T>}
 *
 * @author zhou
 * @date 2023/4/12
 */
@Component("restartItemReaderDemoStreamReader")
public class RestartItemReaderDemoStreamReader implements ItemStreamReader<Customer> {

    /**
     * 直接使用一个读数据的 Reader
     */
    private final FlatFileItemReader<Customer> reader;

    /**
     * 当前执行到的行数
     */
    private int curLine = 0;

    /**
     * 当前执行行数存在 ExecutionContext 的 key
     */
    private static final String KEY = "curLine";

    /**
     * 是否要重新开始
     */
    private boolean shouldRestart = false;

    /**
     * ExecutionContext 用于存储 Step 的状态
     */
    private ExecutionContext executionContext;

    /**
     * 在构造器中，初始化一些内容
     */
    public RestartItemReaderDemoStreamReader() {
        // 初始化 Reader
        reader = new FlatFileItemReader<>();
        // 设置资源路径
        reader.setResource(new ClassPathResource("restart_demo_file.txt"));
        reader.setLinesToSkip(1);

        // 设置映射：
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "address");
        mapper.setLineTokenizer(tokenizer);

        mapper.setFieldSetMapper(fieldSet -> {
            Customer customer = new Customer();
            customer.setId(fieldSet.readLong("id"));
            customer.setName(fieldSet.readString("name"));
            customer.setAddress(fieldSet.readString("address"));
            return customer;
        });

        mapper.afterPropertiesSet();

        reader.setLineMapper(mapper);
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        // 当前行已经执行成功了，所以要指针移动到下一行
        this.curLine++;

        // 如果要重新开始
        if (shouldRestart) {
            // 前一行
            int previousLine = this.curLine - 1;
            // 因为文件中的第一行也要跳过，所以要 +1
            int skipLine = previousLine + 1;
            reader.setLinesToSkip(skipLine);
            // 重置状态
            shouldRestart = false;
            System.out.println("从第 " + this.curLine + " 行开始重新读取数据");
        }

        // 打开 ExecutionContext，用于获取状态
        this.reader.open(this.executionContext);

        // 读取数据
        Customer customer = this.reader.read();

        // 如果读到这个名字时，需要抛出异常，用于模拟错误
        String nameToThrowException = "wrong_name";
        if (Objects.nonNull(customer) && nameToThrowException.equals(customer.getName())) {
            throw new RuntimeException("ID 为 " + customer.getId()
                    + " 的数据的 Name 是 " + nameToThrowException + "，需要抛出异常");
        }
        //  如果没有数据，就将行数减回去
        if (Objects.isNull(customer)) {
            curLine--;
        }

        return customer;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // 初始化 ExecutionContext
        // 注意一下，在运行期间，只有初始化的时候会走到这里
        // 也就是说，这里只是制定规则，只执行一次
        this.executionContext = executionContext;
        // 如果有 curLine 的 key
        if (executionContext.containsKey(KEY)) {
            this.curLine = executionContext.getInt(KEY);
            this.shouldRestart = true;
            return;
        }

        // 如果没有 curLine 的 key
        this.curLine = 0;
        executionContext.put(KEY, this.curLine);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // 在更新数据的时候，把 curLine 的值更新到 ExecutionContext 中
        System.out.println("ExecutionContext 更新 curLine: " + this.curLine);
        executionContext.put(KEY, this.curLine);
    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("结束记录 ExecutionContext");
    }
}