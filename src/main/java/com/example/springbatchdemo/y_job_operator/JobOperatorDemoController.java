package com.example.springbatchdemo.y_job_operator;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送请求，通过 JobOperator 启动 Job
 *
 * @author zhou
 * @date 2023/4/20
 */
@RestController
@RequestMapping("/job-operator")
public class JobOperatorDemoController {

    private final JobOperator jobOperator;

    public JobOperatorDemoController(JobOperator jobOperator) {this.jobOperator = jobOperator;}

    @GetMapping
    public String runJobWithJobOperator(@RequestParam("param") String param) {
        try {
            // 第一个参数是 Job 的名称，第二个参数是 the parameters to launch it with
            // 返回 the id of the JobExecution that is launched
            Long id = jobOperator.start("jobOperatorDemoJob", "demoParam=" + param);
            return "启动成功,id=" + id;
        } catch (NoSuchJobException | JobInstanceAlreadyExistsException | JobParametersInvalidException e) {
            e.printStackTrace();
            return "启动失败";
        }
    }
}
