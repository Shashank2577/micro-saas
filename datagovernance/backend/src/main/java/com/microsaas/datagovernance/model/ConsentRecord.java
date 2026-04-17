package com.microsaas.datagovernance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consent_record")
public class ConsentRecord {
    @Id
    private UUID id;
    private UUID tenantId;
    private String userEmail;
    private String processingPurpose;
    private Boolean isGranted;
    private LocalDateTime timestamp;

    public ConsentRecord() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getProcessingPurpose() { return processingPurpose; }
    public void setProcessingPurpose(String processingPurpose) { this.processingPurpose = processingPurpose; }
    public Boolean getGranted() { return isGranted; }
    public void setGranted(Boolean granted) { isGranted = granted; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
