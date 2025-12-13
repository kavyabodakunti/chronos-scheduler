package com.chronos.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Entity
@Data
@RequiredArgsConstructor
@Table(name = "job_run")
public class JobRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   // private Long jobId;
    private LocalDateTime startedAt;
    private  LocalDateTime finishedAt;
    private String status;
    private String logs;
    private Integer retryCount;

     @ManyToOne
    @JoinColumn(name = "job_id")
    private JobDefinition job;

	

}
