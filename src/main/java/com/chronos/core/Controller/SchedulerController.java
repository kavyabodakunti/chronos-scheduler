package com.chronos.core.Controller;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronos.core.services.ExecutionService;
import com.chronos.core.services.SchedulingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scheduler")
public class SchedulerController {
	private final ExecutionService executionService;
	private final SchedulingService schedulingService;
	private final Scheduler scheduler;
	 /** 🔹 Manually trigger a job immediately */
	@PostMapping("/trigger/{jobId}")
		public ResponseEntity<String> triggerNow(@PathVariable Long jobId) {
	        executionService.executeScheduledJob(jobId);
	        return ResponseEntity.ok("Job executed manually: " + jobId);
	    }	
	
	//reload all jobs
	@PostMapping("/reload")
	public ResponseEntity<String> reloadJobs(){
		schedulingService.reloadAllJobsFromDB();
		return ResponseEntity.ok("Active jobs reloaded into Quartz.");
	}
	
	
	 /** 🔹 Unschedule job from Quartz */
    @DeleteMapping("/unschedule/{jobId}")
    public ResponseEntity<String> unschedule(@PathVariable Long jobId) {
        schedulingService.unscheduleJob(jobId);
        return ResponseEntity.ok("Job unscheduled: " + jobId);
    }
    
    
    /** 🔹 Clear all scheduled Quartz jobs */
    @PostMapping("/clear")
    public ResponseEntity<String> clearScheduler() throws SchedulerException {
        scheduler.clear();
        return ResponseEntity.ok("All Quartz jobs cleared");
    }

	}



