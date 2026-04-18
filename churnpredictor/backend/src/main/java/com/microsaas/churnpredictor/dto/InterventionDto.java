package com.microsaas.churnpredictor.dto;




import java.time.OffsetDateTime;
import java.util.UUID;


public class InterventionDto {
    private UUID id;
    private UUID customerId;
    private UUID playbookId;
    private String status;
    private String offerDetails;
    private String effectivenessStatus;
    private OffsetDateTime executedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getPlaybookId() {
        return playbookId;
    }

    public void setPlaybookId(UUID playbookId) {
        this.playbookId = playbookId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(String offerDetails) {
        this.offerDetails = offerDetails;
    }

    public String getEffectivenessStatus() {
        return effectivenessStatus;
    }

    public void setEffectivenessStatus(String effectivenessStatus) {
        this.effectivenessStatus = effectivenessStatus;
    }

    public OffsetDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(OffsetDateTime executedAt) {
        this.executedAt = executedAt;
    }
}
