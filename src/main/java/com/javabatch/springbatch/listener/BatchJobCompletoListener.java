package com.javabatch.springbatch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class BatchJobCompletoListener implements JobExecutionListener {

    public void beforeJob(JobExecution jobExecution) {
        System.out.println("BEFORE JOB. STATUS ==> " + jobExecution.getStatus());
    }

    public void afterJob(JobExecution jobExecution) {

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("BATCH JOB COMPLETED SUCCESSFULLY");

        } else {
            System.out.println("BATCH JOB ERRO");
        }

    }

}
