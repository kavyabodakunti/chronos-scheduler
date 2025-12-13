package com.chronos.Entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "job_definition")
public class JobDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long  UserId;
    private String jobType;
    private String scheduleType;
    private String cronExpression;
    private LocalDateTime runAt;
    private String requestUrl;
    private String requestBody;
     private boolean active=true;
     private Integer maxRetries;
	


}
