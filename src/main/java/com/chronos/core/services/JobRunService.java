package com.chronos.core.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chronos.Entity.JobRun;
import com.chronos.Repository.JobRunRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
@RequiredArgsConstructor
public class JobRunService {
	
	
	private final JobRunRepository repo;

    public List<JobRun> getAllRuns() {
        return repo.findAll();
    }

//    public List<JobRun> getRunsByJob(Long jobId) {
//        return repo.findByJobId(jobId);
//    }

    public JobRun getRun(Long runId) {
        return repo.findById(runId)
                .orElseThrow(() -> new RuntimeException("Run not found"));
    }
}
