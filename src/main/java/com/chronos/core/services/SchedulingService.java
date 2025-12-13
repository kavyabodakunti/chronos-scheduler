package com.chronos.core.services;


import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chronos.Entity.JobDefinition;
import com.chronos.Repository.JobDefinitionRepository;
import com.chronos.core.job.ChronosQuartzJob;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingService { 
    private final Scheduler scheduler;
   @Autowired
   private JobDefinitionRepository jobRepo;

// Schedule or Reschedule a job
public void scheduleJob(JobDefinition jobDef){
if(!jobDef.isActive()){
    log.info("Job {} is inactive, skipping scheduling",jobDef.getId());
    return;
}
try{
    JobDetail jobDetail=buildJobDetail(jobDef);
    Trigger trigger=buildTrigger(jobDef);
    scheduler.scheduleJob(jobDetail,trigger);
    log.info("Scheduled job {} with trigger {}",jobDef.getId(),trigger.getKey());
}catch(SchedulerException  e){
  throw new RuntimeException("Failed to schedule job: " + e.getMessage());  
}
}
// Unschedule a job
public void unscheduleJob(Long jobId){
    try{
    TriggerKey triggerKey = TriggerKey.triggerKey("trigger_" + jobId);
  if(scheduler.checkExists(triggerKey)){
    scheduler.unscheduleJob(triggerKey);
    log.info("unscheduled Job{}",jobId);
  }
    }catch(SchedulerException e){
        log.error("failed to unschedule job{}",jobId,e);
    }
}

//Build quartz JobDetail
private JobDetail buildJobDetail(JobDefinition jobDef) {
    return JobBuilder.newJob(ChronosQuartzJob.class)
            .withIdentity("job_" + jobDef.getId())
            .usingJobData("jobId", jobDef.getId())
            .storeDurably()
            .build();
}



//    private JobDetail buildJobDetail(JobDefinition jobDef) {
//
//        return JobBuilder.newJob()
//                .withIdentity("job_" + jobDef.getId())
//                .usingJobData("jobId", jobDef.getId())
//                .storeDurably()
//                .build();
//    }
    //built trigger
    private Trigger buildTrigger(JobDefinition jobDef){
   TriggerBuilder<Trigger> builder=TriggerBuilder.newTrigger()
   .withIdentity(("trigger_"+jobDef.getId()));
   switch (jobDef.getScheduleType()) {
    case "cron":
       if (jobDef.getCronExpression() == null) {
                    throw new RuntimeException("Cron expression required for CRON jobs");
                } 
       return builder
                        .withSchedule(CronScheduleBuilder.cronSchedule(jobDef.getCronExpression()))
                        .build(); 
       default:
                throw new RuntimeException("Invalid schedule type"); 
        
   }
    }
    // Load all Active Jobs on Application Startup
    public void reloadAllActiveJobs(Iterable<JobDefinition>jobs){
        for(JobDefinition job:jobs){
            if(job.isActive()){
                scheduleJob(job);
            }
        }
    }
    public void reloadAllJobsFromDB() {
    	List<JobDefinition> jobs = jobRepo.findAll();
        reloadAllActiveJobs(jobs);
    }

}
