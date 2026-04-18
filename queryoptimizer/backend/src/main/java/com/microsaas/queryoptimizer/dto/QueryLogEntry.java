package com.microsaas.queryoptimizer.dto;

import java.time.OffsetDateTime;

public class QueryLogEntry {
    private String query;
    private Double executionTimeMs;
    private String databaseUser;
    private String databaseType;
    private OffsetDateTime executedAt;
    private String executionPlan;

    // Getters and setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public Double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(Double executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public String getDatabaseUser() { return databaseUser; }
    public void setDatabaseUser(String databaseUser) { this.databaseUser = databaseUser; }
    public String getDatabaseType() { return databaseType; }
    public void setDatabaseType(String databaseType) { this.databaseType = databaseType; }
    public OffsetDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(OffsetDateTime executedAt) { this.executedAt = executedAt; }
    public String getExecutionPlan() { return executionPlan; }
    public void setExecutionPlan(String executionPlan) { this.executionPlan = executionPlan; }
}
