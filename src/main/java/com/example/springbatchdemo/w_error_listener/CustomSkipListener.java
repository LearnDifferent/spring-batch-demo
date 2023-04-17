package com.example.springbatchdemo.w_error_listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

/**
 * 发生错误时的监听器
 *
 * @author zhou
 * @date 2023/4/17
 */
@Component("customSkipListener")
public class CustomSkipListener implements SkipListener<Integer, Integer> {

    @Override
    public void onSkipInRead(Throwable throwable) {
        System.err.println("在读取数据时错误的监听器：" + throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(Integer integer, Throwable throwable) {
        System.err.println("在写入数据时错误的监听器 - 数据：" + integer
                + " - 错误：" + throwable.getMessage());
    }

    @Override
    public void onSkipInProcess(Integer integer, Throwable throwable) {
        System.err.println("在处理数据时错误的监听器 - 数据：" + integer
                + " - 错误：" + throwable.getMessage());
    }
}
