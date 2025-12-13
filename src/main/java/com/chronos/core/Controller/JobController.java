package com.chronos.core.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronos.Entity.JobDefinition;
import com.chronos.core.services.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jobs")
public class JobController {
    
private final JobService jobService;
 /** Create a new job */
    @PostMapping
    public ResponseEntity<JobDefinition> createJob(@RequestBody JobDefinition job){
      JobDefinition newJob=  jobService.createJob(job);
      return ResponseEntity.ok(newJob);
    }

//get all jobs
@GetMapping
public ResponseEntity<List<JobDefinition>> getAllJobs(){
return ResponseEntity.ok(jobService.listJobs());
}

//get job By id
@GetMapping("/{id}")
public ResponseEntity<JobDefinition> getJobBYId(@PathVariable Long id){
    return ResponseEntity.ok(jobService.getJob(id));

}
//update job by
@PutMapping("/{id}")
public ResponseEntity<JobDefinition> updateJob(@PathVariable Long id,@RequestBody JobDefinition updatedJob ){
JobDefinition result=jobService.updateJob(id,updatedJob);
return ResponseEntity.ok(result);
}

@DeleteMapping("/{id}")
public ResponseEntity<String> deleteJob(@PathVariable Long id){
    jobService.deleteJob(id);
    return ResponseEntity.ok("Job Deleted Successfully");
}
//active || inactive
@PutMapping("/{id}/checkStatus")
public ResponseEntity<String> checkstatus(@PathVariable Long id){
    JobDefinition job=jobService.getJob(id);
    boolean Status=!job.isActive();
    jobService.setJobActive(id,Status);
    return ResponseEntity.ok("Job is now " + (Status ? "ACTIVE" : "INACTIVE"));
}
}
