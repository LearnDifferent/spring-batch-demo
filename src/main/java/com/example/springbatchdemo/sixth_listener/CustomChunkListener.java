package com.example.springbatchdemo.sixth_listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * 通过注解的方式实现 Listener
 *
 * @author zhou
 * @date 2023/3/31
 */
public class CustomChunkListener {

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        String name = chunkContext.getStepContext().getStepName();
        System.out.println("Chunk Listener - Before: " + name);
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        String name = chunkContext.getStepContext().getStepName();
        System.out.println("Chunk Listener - After: " + name);
    }
}
