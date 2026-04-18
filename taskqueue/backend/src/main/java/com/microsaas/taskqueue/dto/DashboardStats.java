package com.microsaas.taskqueue.dto;

public class DashboardStats {
    private long totalJobs;
    private long pendingJobs;
    private long runningJobs;
    private long completedJobs;
    private long failedJobs;
    private long deadLetterJobs;
    private double successRate;

    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }
    public long getPendingJobs() { return pendingJobs; }
    public void setPendingJobs(long pendingJobs) { this.pendingJobs = pendingJobs; }
    public long getRunningJobs() { return runningJobs; }
    public void setRunningJobs(long runningJobs) { this.runningJobs = runningJobs; }
    public long getCompletedJobs() { return completedJobs; }
    public void setCompletedJobs(long completedJobs) { this.completedJobs = completedJobs; }
    public long getFailedJobs() { return failedJobs; }
    public void setFailedJobs(long failedJobs) { this.failedJobs = failedJobs; }
    public long getDeadLetterJobs() { return deadLetterJobs; }
    public void setDeadLetterJobs(long deadLetterJobs) { this.deadLetterJobs = deadLetterJobs; }
    public double getSuccessRate() { return successRate; }
    public void setSuccessRate(double successRate) { this.successRate = successRate; }
}
