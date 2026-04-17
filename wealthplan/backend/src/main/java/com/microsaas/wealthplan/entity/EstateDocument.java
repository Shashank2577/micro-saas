package com.microsaas.wealthplan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "estate_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstateDocument {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private String type;
    private boolean isCompleted;
    private LocalDate lastUpdatedDate;
    private String notes;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
