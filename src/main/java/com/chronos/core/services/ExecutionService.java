package com.chronos.core.services;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

import com.chronos.Entity.JobDefinition;
import com.chronos.Entity.JobRun;
import com.chronos.Repository.JobDefinitionRepository;
import com.chronos.Repository.JobRunRepository;
import com.chronos.core.executors.HttpExecutor;
import com.chronos.core.job.ChronosQuartzJob;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutionService {
private final  JobDefinitionRepository jobDefinitionRepository;
private final JobRunRepository jobRunRepository;
private final  HttpExecutor httpExecutor;
private final Scheduler scheduler;
private final NotificationService notificationService;
// Called from Quartz → ChronosQuartzJob
@Transactional
public void executeScheduledJob(Long jobId){
   JobDefinition job= jobDefinitionRepository.findById(jobId)
   .orElseThrow(()->new RuntimeException("Job not found:"+jobId));
   log.info("Executing job {} ({})",jobId,job.getName());

   // Create JobRun entry (RUNNING)
   JobRun run=new JobRun();
  run.setJob(job);
        run.setStatus("RUNNING");
        run.setStartedAt(LocalDateTime.now());
        run.setRetryCount(0);
        jobRunRepository.save(run);
        
        try{
           // Execute based on jobType
        String result=executeJob(job); 
        // SUCCESS
            run.setStatus("SUCCESS");
            run.setFinishedAt(LocalDateTime.now());
            run.setLogs(result);
            jobRunRepository.save(run);

            log.info("Job {} succeeded", jobId);
        }catch (Exception ex) {

            log.error("Job {} FAILED: {}", jobId, ex.getMessage());

            run.setStatus("FAILED");
            run.setFinishedAt(LocalDateTime.now());
            run.setLogs(ex.getMessage());
            jobRunRepository.save(run);

            handleRetry(job, run);
        }
    }

// Execute the actual job (HTTP for now)
    // ---------------------------------------------
    private String executeJob(JobDefinition job) throws Exception {

        switch (job.getJobType().toUpperCase()) {

            case "HTTP":
                return httpExecutor.execute(job);

            default:
                throw new IllegalArgumentException("Unsupported job type: " + job.getJobType());
        }
    }
    // Simple retry mechanism
    // ---------------------------------------------
    private void handleRetry(JobDefinition job, JobRun run) {

        int maxRetries = job.getMaxRetries() == null ? 0 : job.getMaxRetries();
        
        if (run.getRetryCount() >= maxRetries) {
            log.error("Job {} failed after all retries", job.getId());
            
         // SEND NOTIFICATION HERE
            notificationService.notifyFailure(
                    job,
                    run,
                    "Job permanently failed after " + maxRetries + " retries"
            );
            return;
        }

        
            int nextRetry = run.getRetryCount() + 1;
            run.setRetryCount(nextRetry);
            jobRunRepository.save(run);

            log.info("Scheduling retry {} for job {}...", nextRetry, job.getId());

            try {
                JobDetail retryJob = JobBuilder.newJob(ChronosQuartzJob.class)
                        .withIdentity("retry_" + job.getId() + "_" + nextRetry)
                        .usingJobData("jobId", job.getId())
                        .build();

                Trigger retryTrigger = TriggerBuilder.newTrigger()
                        .startAt(Date.from(
                                LocalDateTime.now().plusSeconds(10)
                                        .atZone(ZoneId.systemDefault()).toInstant()
                        ))
                        .build();

                scheduler.scheduleJob(retryJob, retryTrigger);

            } catch (Exception e) {
                log.error("Failed to schedule retry for job {}", job.getId(), e);
            }}
    }



