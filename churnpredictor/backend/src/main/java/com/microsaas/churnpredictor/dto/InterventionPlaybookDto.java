package com.microsaas.churnpredictor.dto;




import java.time.OffsetDateTime;
import java.util.UUID;


public class InterventionPlaybookDto {
    private UUID id;
    private String name;
    private String description;
    private String triggerRiskSegment;
    private String actionType;
    private Boolean active;
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTriggerRiskSegment() {
        return triggerRiskSegment;
    }

    public void setTriggerRiskSegment(String triggerRiskSegment) {
        this.triggerRiskSegment = triggerRiskSegment;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
