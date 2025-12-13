package com.chronos.core.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chronos.core.services.ExecutionService;

@Component
public class ChronosQuartzJob implements Job {

    @Autowired
    private ExecutionService executionService;

    @Override
    public void execute(JobExecutionContext context) {
        Long jobId = context.getJobDetail()
                            .getJobDataMap()
                            .getLong("jobId");

        executionService.executeScheduledJob(jobId);
    }
}