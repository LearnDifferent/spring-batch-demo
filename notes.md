# Spring Batch

## 基础概念

### 批处理

**批处理**：

- 对有限数据（Finite Data）的处理
  
  - the data can be processed to complete

- 无需交互（No Interaction）
  
  - Interaction 的例子：Web Request（Web 请求）和 Message（发送消息）
  
  - Batch Processing Program run on the server without the need of interaction

- 任务是无外界中断的（No Interruption）
  
  - From start to end

> Non-Interactive application can consider using Batch Processing.
> 
> Spring Batch is one of the solution.

### Job

**Job**：

- `Job` defines what a job is and how it is to be executed.

- `JobInstance` 是一个逻辑单位，表示 `Job` 的实例化对象。每次运行 `Job` 都会生成一个 `JobInstance` 。

- `JobExecution` 就是 `JobInstance` 的实际 尝试执行。Each `JobInstance` can have multiple executions .

How is one `JobInstance` distinguished from another? The answer is `JobParameters`.

**A `JobParameters` object holds a set of parameters used to start a batch job.**

`JobParameters` 可以是时间参数，比如今天执行了时间参数为 2023-03-28 的 job 之后（且状态为已完成），如果第二天还要执行时间参数为 2023-03-28 的 job 就会提示已经完成了。

<u>判断一个 Job 是否要执行，就是看 `JobParameters` 和 status（完成状态）。</u>

---

假设一个 `JobInstance` 在执行 `JobParameters` 为 2023-03-28 的 job 时失败了，此时算作一个 `JobExecution` 。

等下次再执行相同的 `JobParameters` 为 2023-03-28 的 job 时，a new `JobExecution` is created.

However, there is still only one `JobInstance`.

### Step

`Job` 是由一个个 `Step` 构成。

A `Step` contains all of the information necessary to define and control the actual batch processing.

---

Step 分类：

- Tasklet
  
  - an interface with one execute() method
  
  - 直接执行

- Chunk-based
  
  - 定义：item-based to process the item one by one
  
  - 步骤 1：`ItemReader` 用于 input（输入数据、读取数据）
  
  - 步骤 2：`ItemProcessor` 用于 processing (optional)（处理数据）
  
  - 步骤 3：`ItemWriter` 用于 output（输出数据）

---

`StepExecution` represents a single attempt to execute a `Step`.

A new `StepExecution` is created each time a `Step` is actually started, similar to `JobExecution`. 

### ExecutionContext

An ExecutionContext（执行上下文）represents <u>a collection of key/value pairs</u> that are persisted and controlled by the framework in order to allow developers a place to store persistent state.

There is at least one ExecutionContext per `JobExecution` and one for every `StepExecution`.

对于 `StepExecution` 而言，在每次 commit point 的时候才去持久化。假设设置了每读取 100 行做一次操作，那么这个 commit point 就是读取 100 行。

而 `JobExecution` 在每次 `StepExecution` 都会持久化。

### JobRepository

JobRepository 是 Job 的持久化机制。

When a `Job` is first launched, a `JobExecution` is obtained from the repository, and, during the course of execution, `StepExecution` and `JobExecution` implementations are persisted by passing them to the repository.

也就是说，JobRepository 是将 `JobExecution` 和 `StepExecution` 的执行进行持久化（存入数据库等）的机制

### JobLauncher

`JobLauncher` is a simple interface for launching a `Job` with a given set of `JobParameters`.

定义某个 `Job` 在某个 `JobParameters` 的情况下执行，生成 `JobExecution`：

```java
public interface JobLauncher {
    JobExecution run(Job job, JobParameters jobParameters ) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException;
}
```

### ItemReader, ItemProcesser and ItemWriter

`ItemReader` is used to retrieve the input data for a `Step` <u>one item at a time</u>.

`ItemProcesser` 用于处理数据。

`ItemWriter` is used to output data for a `Step` <u>one batch or chunck of items at a time</u>.

### Job Flow

Job Flow 是多个 Step 的集合，并定义了多个 Step 之间的关系，用于控制多个 Step 的执行顺序。

使用 Flow 的主要目的是复用 -- Flow can be reused within a `Job`

### Split

使用 Split 可以并发地执行多个 Flow (Execute multiple flows in parallel)

### JobExecutionDecider

`JobExecutionDecider` （决策器）可以主动地控制状态流转。

如果没有 `JobExecutionDecider` ，那么它是通过 Step 返回的 `ExitStatus` 来控制流程的。

如果 implements 了 `JobExecutionDecider` 接口，那么它通过重写 `public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution)` 方法来返回自定义的 `FlowExecutionStatus` ，以此来控制流程状态。

### Nested Job

One `Job` can be nested in another `Job`. We call the nested job the child job, while the nesting job the parent job.

The child job will not execute separately but be launched by the parent job.

注意：Nested Job 需要到配置文件中配置 spring.batch.job.names 为当前的 Parent Job 的 Bean，这样的话就可以指定只启动这个 Parent Job，其他的 Child Job 只会通过这个 Parent Job 来启动。

如果没有配置 spring.batch.job.names，那么 Child Job，除了被 Parent Job 启动之外，自己还会启动一次。

### Listener

Spring Batch 的监听器可以细分出多种：

- JobExecutionListener: before, after
- StepExecutionListener: before, after
- ChunkListener: before, after, error
- ItemReadListener, ItemProcessListener, ItemWriterListener: before, after, error

Listener 可以通过注解或实现接口来实现。