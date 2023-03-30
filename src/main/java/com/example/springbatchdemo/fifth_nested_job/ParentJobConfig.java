package com.example.springbatchdemo.fifth_nested_job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Parent Job.
 * 需要注意，因为定义了多个 Child Job，而这些 Child Job 实际上只想运行一次
 * ，所以需要到配置文件中配置 spring.batch.job.names 为当前的 Job 的 Bean。
 * 只有这样配置才能通过运行 parentJob 来运行其 Child Job
 * ，如果不这样配置每个 Child Job 都会启动。
 * 这里创建了一个 application-nested-job.yml 用于专门配置这个 Demo。
 *
 * @author zhou
 * @date 2023/3/30
 */
@Configuration
public class ParentJobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    /**
     * 引入 firstChildJob 和 secondChildJob 的 Bean
     */
    private final Job firstChildJob;
    private final Job secondChildJob;

    /**
     * 引入 JobLauncher 用于启动 Child Job
     */
    private final JobLauncher jobLauncher;
    /**
     * 引入持久化机制
     */
    private final JobRepository jobRepository;
    /**
     * 引入事务
     */
    private final PlatformTransactionManager ptm;

    public ParentJobConfig(JobBuilderFactory jobBuilderFactory,
                           Job firstChildJob,
                           Job secondChildJob,
                           JobLauncher jobLauncher,
                           JobRepository jobRepository, PlatformTransactionManager ptm) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.firstChildJob = firstChildJob;
        this.secondChildJob = secondChildJob;
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
        this.ptm = ptm;
    }

    @Bean
    public Job parentJob() {
        return jobBuilderFactory
                .get("parentJob")
                .start(childJob1())
                .next(childJob2())
                .build();
    }

    /**
     * 通过 decorator（装饰器）将 Job 转换为 Step
     *
     * @return 将 Child Job 转换为 Step 并返回
     */
    @Bean
    public Step childJob1() {
        return new JobStepBuilder(new StepBuilder("childJob1"))
                // Job 是被 Spring 管理的 firstChildJob
                .job(firstChildJob)
                // Job Launcher 用于启动这个 Child Job
                .launcher(jobLauncher)
                // 持久化
                .repository(jobRepository)
                // 事务
                .transactionManager(ptm)
                .build();
    }

    @Bean
    public Step childJob2() {
        return new JobStepBuilder(new StepBuilder("childJob2"))
                .job(secondChildJob)
                .repository(jobRepository)
                .transactionManager(ptm)
                .build();
    }
}