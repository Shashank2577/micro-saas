package com.microsaas.securitypulse;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "findings")
public class Finding {

    @Id
    private UUID id;
    private String prUrl;
    private String tool;
    private String severity;
    private String message;
    private String status;
    private UUID tenantId;

    public Finding() {
    }

    public Finding(UUID id, String prUrl, String tool, String severity, String message, String status, UUID tenantId) {
        this.id = id;
        this.prUrl = prUrl;
        this.tool = tool;
        this.severity = severity;
        this.message = message;
        this.status = status;
        this.tenantId = tenantId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrUrl() {
        return prUrl;
    }

    public void setPrUrl(String prUrl) {
        this.prUrl = prUrl;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }
}
