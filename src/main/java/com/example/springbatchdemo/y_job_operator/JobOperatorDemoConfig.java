package com.example.springbatchdemo.y_job_operator;

import java.util.Map;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link JobOperator} 是对 {@link JobLauncher} 进行封装，我们需要手动设置属性才能启动。
 *
 * @author zhou
 * @date 2023/4/20
 */
@Configuration
@EnableBatchProcessing
public class JobOperatorDemoConfig implements StepExecutionListener, ApplicationContextAware {

    /**
     * JobParameters
     */
    private Map<String, JobParameter> parameters;

    private ApplicationContext applicationContext;

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private final JobRepository jobRepository;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public JobOperatorDemoConfig(JobBuilderFactory jobBuilderFactory,
                                 StepBuilderFactory stepBuilderFactory,
                                 JobLauncher jobLauncher,
                                 JobRegistry jobRegistry,
                                 JobExplorer jobExplorer,
                                 JobRepository jobRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
        this.jobExplorer = jobExplorer;
        this.jobRepository = jobRepository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 通过 Job 的 name 找到对应的 Bean
     *
     * @return {@link JobRegistryBeanPostProcessor}
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
        JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
        // 设置 JobRegistry，用于获取所有 Job 的 name
        processor.setJobRegistry(jobRegistry);
        // 设置 Spring 容器的 Bean 工厂，用于将 Job 的 name 和 Spring 的 Bean 相对应
        processor.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
        processor.afterPropertiesSet();
        return processor;
    }

    /**
     * 自己创建实现一个 JobOperator
     *
     * @return JobOperator
     */
    @Bean
    public JobOperator jobOperator() {
        // 用 SimpleJobOperator 来实现 JobOperator
        SimpleJobOperator operator = new SimpleJobOperator();

        // 设置 JobLauncher
        operator.setJobLauncher(jobLauncher);

        // 设置 JobRepository
        operator.setJobRepository(jobRepository);

        // 设置如何将参数转化为 JobParameters
        // 这里使用默认的转换器，也就是将 “KEY=VALUE” 这样使用 = （等于号）的 key value 字符串转换为 JobParameters
        operator.setJobParametersConverter(new DefaultJobParametersConverter());

        // 这里要求填入的是 ListableJobLocator，其是用于获取所有 Job 名称的
        // 这里使用一个 Spring 生成的 JobRegistry 类型的实例
        // JobRegistry 接口：public interface JobRegistry extends ListableJobLocator
        operator.setJobRegistry(jobRegistry);

        // JobExplorer: Entry point for browsing the executions of running or historical jobs and steps
        operator.setJobExplorer(jobExplorer);

        return operator;
    }

    @Bean
    public Job jobOperatorDemoJob() {
        return jobBuilderFactory
                .get("jobOperatorDemoJob")
                .start(jobOperatorDemoStep())
                .build();
    }

    @Bean
    public Step jobOperatorDemoStep() {
        return stepBuilderFactory
                .get("jobOperatorDemoStep")
                .listener(this)
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("当前 Step 包含的 JobParameters 如下：");
                    parameters
                            .forEach((k, v) -> System.out.println(
                                    "key = " + k + ", JobParameter 的 value = " + v.getValue()));
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
