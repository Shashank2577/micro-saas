package com.microsaas.taskqueue.dto;

import com.microsaas.taskqueue.domain.JobPriority;

public class ScheduledTaskRequest {
    private String name;
    private String cronExpression;
    private String jobName;
    private String payloadTemplate;
    private JobPriority priority = JobPriority.NORMAL;
    private Boolean active = true;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCronExpression() { return cronExpression; }
    public void setCronExpression(String cronExpression) { this.cronExpression = cronExpression; }
    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }
    public String getPayloadTemplate() { return payloadTemplate; }
    public void setPayloadTemplate(String payloadTemplate) { this.payloadTemplate = payloadTemplate; }
    public JobPriority getPriority() { return priority; }
    public void setPriority(JobPriority priority) { this.priority = priority; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
