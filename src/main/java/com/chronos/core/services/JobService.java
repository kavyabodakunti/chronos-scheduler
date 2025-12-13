package com.chronos.core.services;

import java.util.List;


import org.springframework.stereotype.Service;

import com.chronos.Entity.JobDefinition;
import com.chronos.Repository.JobDefinitionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

  private final JobDefinitionRepository jobDefinitionRepository;
    private final SchedulingService schedulingService;


    //create Job

@Transactional
public JobDefinition createJob(JobDefinition job){
    validateJob(job);
  JobDefinition saved=   jobDefinitionRepository.save(job);
  schedulingService.scheduleJob(saved);
  return saved;

}

//Update Job
@Transactional
public JobDefinition updateJob(Long id, JobDefinition updates){
    JobDefinition existing=jobDefinitionRepository.findById(id)
    .orElseThrow(()->new RuntimeException("Job Not Found"));
    existing.setName(updates.getName());
    existing.setJobType(updates.getJobType());
    existing.setScheduleType(updates.getScheduleType());
    existing.setCronExpression(updates.getCronExpression());
    existing.setRunAt(updates.getRunAt());
    existing.setRequestUrl(updates.getRequestUrl());
    existing.setRequestBody(updates.getRequestBody());
    existing.setActive(updates.isActive()); 
    
    validateJob(existing);
    JobDefinition saved=jobDefinitionRepository.save(existing);
    schedulingService.scheduleJob(saved);
    return saved;
}
// Get job by ID
@Transactional
public JobDefinition getJob(Long id){
  return jobDefinitionRepository.findById(id)
  .orElseThrow(()->new RuntimeException("Job not found"));
}

//Get All /Jobs
public List<JobDefinition> listJobs (){
    return jobDefinitionRepository.findAll();
}
//delete job
@Transactional
public void deleteJob(Long id){
    JobDefinition job=jobDefinitionRepository.findById(id).
    orElseThrow(()->new RuntimeException("Job not found"));
    schedulingService.unscheduleJob(job.getId());
    jobDefinitionRepository.delete(job);
}
//Activate /Deactivate Job
@Transactional
public JobDefinition setJobActive(Long id,boolean active){
 JobDefinition job=jobDefinitionRepository.findById(id).
 orElseThrow(()->new RuntimeException("Job not found"));
    job.setActive(active);
    jobDefinitionRepository.save(job);
    if(active){
        schedulingService.scheduleJob(job);
    }else{
        schedulingService.unscheduleJob(job.getId());
    }
    return job;
}

//validate Job
public void validateJob(JobDefinition job){
    if(job.getName()==null||job.getName().isBlank()){
      throw new RuntimeException("Job name is required");  
    }
    if(job.getJobType()==null){
      throw new RuntimeException("Job type is required");  
    }
    if(job.getScheduleType()==null){
      throw new RuntimeException("Schedule type is required");  
    }

    //validate Schedule
    if("ONCE".equalsIgnoreCase(job.getScheduleType())){
        if(job.getRunAt()==null){
          throw new RuntimeException("runAt must be provided for ONCE jobs");  
        }
    }
    if("CRON".equalsIgnoreCase(job.getScheduleType())){
        if(job.getCronExpression()==null||job.getCronExpression().isBlank()){
          throw new RuntimeException("cronExpression is required for CRON jobs");  
        }
    }
}
}
