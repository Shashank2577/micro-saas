package com.crosscutting.starter.queue;

import java.time.Instant;

public class Job {

    private Long id;
    private String queueName;
    private String payload;
    private int attemptCount;
    private int maxAttempts;
    private Instant createdAt;
    private Instant scheduledAt;

    public Job() {
    }

    public Job(Long id, String queueName, String payload, int attemptCount,
               int maxAttempts, Instant createdAt, Instant scheduledAt) {
        this.id = id;
        this.queueName = queueName;
        this.payload = payload;
        this.attemptCount = attemptCount;
        this.maxAttempts = maxAttempts;
        this.createdAt = createdAt;
        this.scheduledAt = scheduledAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(Instant scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
