# Chronos – Job Scheduling & Execution System

Chronos is a backend job scheduling and execution system built using Spring Boot and Quartz.  
It allows users to define jobs, schedule them, execute them manually or automatically, retry failed jobs, and track execution history.

---

## 🎯 Project Objective

The goal of this project is to design a reliable scheduling system that:
- Executes background jobs
- Handles failures gracefully with retries
- Tracks job execution history
- Notifies users when jobs fail permanently

---

## 🧩 Key Features

- Create and manage scheduled jobs
- Quartz-based job scheduling
- Manual job triggering
- Automatic retry mechanism with delay
- Job execution history tracking
- Failure notification support
- Basic authentication
- Centralized logging

---

## 🏗 High-Level Architecture

Client (Postman / API Client)  
→ Controller Layer  
→ Service Layer  
→ Quartz Scheduler  
→ Job Execution Engine  
→ Retry Handler  
→ Notification Service  

---

## 🧪 Core Concepts

### Job Definition
Defines:
- Job name
- Job type (HTTP)
- Schedule type (CRON)
- Execution configuration
- Retry count
- Active/inactive status

---

### Job Execution
- Jobs are executed by Quartz
- Execution status is recorded
- Success and failure are tracked
- Execution logs are saved

---

### Retry Mechanism
- Failed jobs are retried based on configuration
- Retries are scheduled with delay (Quartz-based)
- No recursive execution
- Job is marked permanently failed after retries

---

### Job Run Tracking
Each execution creates a **JobRun** record:
- Start time
- End time
- Status (RUNNING / SUCCESS / FAILED)
- Retry count
- Execution logs

---

### Failure Notification
- Triggered after final retry failure
- Notification service is invoked
- Supports extension to webhook notifications

---

## 🔐 Security

- Basic authentication is implemented
- Unauthorized access is blocked
- Monitoring endpoints can be exposed publicly if required

---

## 📊 Logging & Monitoring

- Logging implemented using SLF4J
- Tracks:
  - Job execution start
  - Success
  - Failure
  - Retry attempts
- Useful for debugging and monitoring job health

---

## 🔗 API Overview

### Job Management
- Create job
- List jobs

### Scheduler Operations
- Trigger job manually
- Reload scheduled jobs
- Unschedule jobs
- Clear scheduler

### Execution Monitoring
- View all job runs
- View runs for a specific job

---

## 🚀 Outcome

This project demonstrates:
- Clean layered architecture
- Proper use of Quartz Scheduler
- Reliable retry handling
- Transaction-safe execution
- Production-style backend design

---



## 👤 Author

**Anil Kumar**
