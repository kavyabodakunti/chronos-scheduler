package com.chronos.core.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chronos.Entity.JobDefinition;
import com.chronos.Entity.JobRun;
import com.chronos.Entity.NotificationEndPoint;
import com.chronos.Repository.NotificationEndpointRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
private NotificationEndpointRepository notificationEndpointRepository;
private final RestTemplate restTemplate = new RestTemplate();
 /**
     * Called by ExecutionService when job fails permanently.
     */

 public void notifyFailure(JobDefinition job,JobRun run,String reason){
   
    List<NotificationEndPoint> endpoints=notificationEndpointRepository
    .findByUserId(job.getUserId());
    
    for (NotificationEndPoint endpoint : endpoints) {

        if (!endpoint.isActive()) continue;

        switch (endpoint.getEndpointType().toUpperCase()) {

            case "WEBHOOK":
                sendWebhook(endpoint.getEndpointUrl(), job, run, reason);
                break;

            case "EMAIL":
                log.info("Email notification pending implementation");
                break;

            case "SMS":
                log.info("SMS notification pending implementation");
                break;

            default:
                log.warn("Unknown endpoint type: {}", endpoint.getEndpointType());
        }
    }
}

    

 private void sendWebhook(String url, JobDefinition job, JobRun run, String reason) {

     try {
         Map<String, Object> payload = new HashMap<>();
         payload.put("jobId", job.getId());
         payload.put("jobName", job.getName());
         payload.put("status", "FAILED");
         payload.put("reason", reason);
         payload.put("retryCount", run.getRetryCount());
         payload.put("time", LocalDateTime.now().toString());

         restTemplate.postForEntity(url, payload, String.class);

         log.info("🔔 Notification webhook sent → {}", url);

     } catch (Exception e) {
         log.error("Failed to send webhook: {}", e.getMessage());
     }
 }
}
