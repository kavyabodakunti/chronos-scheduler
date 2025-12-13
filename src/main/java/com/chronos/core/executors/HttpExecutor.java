package com.chronos.core.executors;
   

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.chronos.Entity.JobDefinition;

import lombok.extern.slf4j.Slf4j;


// IMPORTANT: FIXED package

@Component
@Slf4j
public class HttpExecutor {

    private final RestTemplate restTemplate = new RestTemplate();

    public String execute(JobDefinition job) throws Exception {

        if (job.getRequestUrl() == null || job.getRequestUrl().isBlank()) {
            throw new IllegalArgumentException("HTTP job requires a requestUrl");
        }

        log.info("Executing HTTP job → {}", job.getRequestUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(job.getRequestBody(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                job.getRequestUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        int statusCode = response.getStatusCode().value();
        log.info("HTTP job response status: {}", statusCode);

        if (statusCode >= 200 && statusCode < 300) {
            return response.getBody() == null ? "OK" : response.getBody();
        } else {
            throw new RuntimeException("HTTP call failed with status " + statusCode);
        }
    }
}
