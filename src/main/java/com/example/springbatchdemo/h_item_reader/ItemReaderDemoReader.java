package com.example.springbatchdemo.h_item_reader;

import java.util.Iterator;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * 演示 ItemReader
 *
 * @author zhou
 * @date 2023/4/3
 */
public class ItemReaderDemoReader implements ItemReader<String> {

    private final Iterator<String> iterator;

    public ItemReaderDemoReader(List<String> data) {
        // 传入 List，获取遍历器
        this.iterator = data.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // 控制每次读取数据的方式
        if (iterator.hasNext()) {
            // 这里演示每次只读取一个数据
            return iterator.next() + " - Read";
        }
        // 如果没数据了，一定要返回 null
        // 也就是说，ItemReader 最后一定是返回 null 的
        return null;
    }
}
