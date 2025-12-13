package com.chronos.core.Controller;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitoringController {
 private final Scheduler scheduler;
 
 @GetMapping("/status")
 public ResponseEntity<String>getSchedulerStatus() throws SchedulerException{
	 boolean started =scheduler.isStarted();
	 boolean standBy=scheduler.isInStandbyMode();
	 boolean shutDown=scheduler.isShutdown();
	 return ResponseEntity.ok("Started: " + started + 
	            " | Standby: " + standBy + 
	            " | Shutdown: " + shutDown
	        );
 }
 @GetMapping("/running")
 public ResponseEntity<Object> getRunnningJobs()throws SchedulerException{
	 return ResponseEntity.ok(scheduler.getCurrentlyExecutingJobs());
 }
}
