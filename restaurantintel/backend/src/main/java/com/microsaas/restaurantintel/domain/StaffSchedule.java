package com.microsaas.restaurantintel.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class StaffSchedule {
    @Id
    private UUID id;
    private UUID tenantId;
    private String role;
    private LocalDate date;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private String predictedBusyness;
    private Integer recommendedStaffCount;
    private Instant createdAt;
    private Instant updatedAt;

    public StaffSchedule() {}

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getShiftStart() { return shiftStart; }
    public void setShiftStart(LocalTime shiftStart) { this.shiftStart = shiftStart; }
    public LocalTime getShiftEnd() { return shiftEnd; }
    public void setShiftEnd(LocalTime shiftEnd) { this.shiftEnd = shiftEnd; }
    public String getPredictedBusyness() { return predictedBusyness; }
    public void setPredictedBusyness(String predictedBusyness) { this.predictedBusyness = predictedBusyness; }
    public Integer getRecommendedStaffCount() { return recommendedStaffCount; }
    public void setRecommendedStaffCount(Integer recommendedStaffCount) { this.recommendedStaffCount = recommendedStaffCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
