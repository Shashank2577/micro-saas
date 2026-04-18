package com.microsaas.churnpredictor.entity;

import jakarta.persistence.*;



import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "interventions")

public class Intervention {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playbook_id", nullable = false)
    private InterventionPlaybook playbook;

    private String status;

    @Column(name = "offer_details")
    private String offerDetails;

    @Column(name = "effectiveness_status")
    private String effectivenessStatus;

    @CreationTimestamp
    @Column(name = "executed_at", updatable = false)
    private OffsetDateTime executedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public InterventionPlaybook getPlaybook() {
        return playbook;
    }

    public void setPlaybook(InterventionPlaybook playbook) {
        this.playbook = playbook;
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
