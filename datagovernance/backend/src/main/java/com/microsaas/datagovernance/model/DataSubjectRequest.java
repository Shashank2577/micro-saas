package com.microsaas.datagovernance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data_subject_request")
public class DataSubjectRequest {
    @Id
    private UUID id;
    private UUID tenantId;
    private String subjectEmail;
    private String requestType; // ACCESS, DELETION
    private String status; // PENDING, IN_PROGRESS, COMPLETED, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public DataSubjectRequest() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getSubjectEmail() { return subjectEmail; }
    public void setSubjectEmail(String subjectEmail) { this.subjectEmail = subjectEmail; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
