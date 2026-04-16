package com.microsaas.dataprivacyai.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "data_flows", schema = "dataprivacyai")
@Getter
@Setter
public class DataFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "destination_system")
    private String destinationSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_category")
    private DataCategory dataCategory;

    @Column(name = "transfer_mechanism")
    private String transferMechanism;

    @Enumerated(EnumType.STRING)
    @Column(name = "legal_basis")
    private LegalBasis legalBasis;

    @Column(name = "tenant_id")
    private UUID tenantId;
}
