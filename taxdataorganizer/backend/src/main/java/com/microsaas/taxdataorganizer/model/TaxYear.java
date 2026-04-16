package com.microsaas.taxdataorganizer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "tax_year", schema = "taxdataorganizer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxYear {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "year", nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaxYearStatus status;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
