package com.microsaas.taskqueue.dto;

import com.microsaas.taskqueue.domain.JobPriority;
import java.util.UUID;

public class JobRequest {
    private String name;
    private JobPriority priority = JobPriority.NORMAL;
    private String payload;
    private Integer maxRetries = 3;
    private Integer timeoutSeconds = 3600;
    private UUID dependsOnJobId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public JobPriority getPriority() { return priority; }
    public void setPriority(JobPriority priority) { this.priority = priority; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public Integer getMaxRetries() { return maxRetries; }
    public void setMaxRetries(Integer maxRetries) { this.maxRetries = maxRetries; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public UUID getDependsOnJobId() { return dependsOnJobId; }
    public void setDependsOnJobId(UUID dependsOnJobId) { this.dependsOnJobId = dependsOnJobId; }
}
