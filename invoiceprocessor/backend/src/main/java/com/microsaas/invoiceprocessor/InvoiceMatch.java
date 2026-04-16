package com.microsaas.invoiceprocessor;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.UUID;

@Entity
@Table(name = "invoice_matches", schema = "invoiceprocessor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "invoice_id", nullable = false)
    private UUID invoiceId;

    @Column(name = "po_id", nullable = false)
    private UUID poId;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_status", nullable = false)
    private MatchStatus matchStatus;

    @Column(name = "discrepancy_description")
    private String discrepancyDescription;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
