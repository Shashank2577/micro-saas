package com.microsaas.brandvoice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "consistency_trends")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsistencyTrend {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private UUID brandProfileId;

    @Column(nullable = false)
    private LocalDate period;

    private BigDecimal avgScore;

    private Integer reviewCount;
}
