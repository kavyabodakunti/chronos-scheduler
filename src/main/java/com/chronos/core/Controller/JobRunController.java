package com.chronos.core.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chronos.Entity.JobRun;
 
import com.chronos.core.services.JobRunService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/runs")
@RequiredArgsConstructor
public class JobRunController {
	private final JobRunService jobRunService;

	@GetMapping()
	public ResponseEntity<List<JobRun>> getAllRuns(){
		return ResponseEntity.ok(jobRunService.getAllRuns());
	}
	
	/** Get single run by id */
    @GetMapping("/{runId}")
    public ResponseEntity<JobRun> getRun(@PathVariable Long runId){
    	return ResponseEntity.ok(jobRunService.getRun(runId));
    }

}
