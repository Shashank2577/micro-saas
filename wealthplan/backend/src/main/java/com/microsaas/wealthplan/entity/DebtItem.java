package com.microsaas.wealthplan.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "debt_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtItem {
    @Id
    private UUID id = UUID.randomUUID();
    private String tenantId;
    private String name;
    private String type;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private BigDecimal minimumPayment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
