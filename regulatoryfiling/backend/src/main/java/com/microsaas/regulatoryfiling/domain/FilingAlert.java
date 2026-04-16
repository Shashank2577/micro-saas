package com.microsaas.regulatoryfiling.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "filing_alerts")
public class FilingAlert {

    @Id
    private UUID id;

    @Column(name = "obligation_id", nullable = false)
    private UUID obligationId;

    @Column(name = "alert_date", nullable = false)
    private LocalDate alertDate;

    @Column(name = "days_until_due", nullable = false)
    private int daysUntilDue;

    @Column(nullable = false)
    private boolean acknowledged;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public FilingAlert() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getObligationId() { return obligationId; }
    public void setObligationId(UUID obligationId) { this.obligationId = obligationId; }

    public LocalDate getAlertDate() { return alertDate; }
    public void setAlertDate(LocalDate alertDate) { this.alertDate = alertDate; }

    public int getDaysUntilDue() { return daysUntilDue; }
    public void setDaysUntilDue(int daysUntilDue) { this.daysUntilDue = daysUntilDue; }

    public boolean isAcknowledged() { return acknowledged; }
    public void setAcknowledged(boolean acknowledged) { this.acknowledged = acknowledged; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public static FilingAlertBuilder builder() {
        return new FilingAlertBuilder();
    }

    public static class FilingAlertBuilder {
        private UUID id;
        private UUID obligationId;
        private LocalDate alertDate;
        private int daysUntilDue;
        private boolean acknowledged;
        private UUID tenantId;

        public FilingAlertBuilder id(UUID id) { this.id = id; return this; }
        public FilingAlertBuilder obligationId(UUID obligationId) { this.obligationId = obligationId; return this; }
        public FilingAlertBuilder alertDate(LocalDate alertDate) { this.alertDate = alertDate; return this; }
        public FilingAlertBuilder daysUntilDue(int daysUntilDue) { this.daysUntilDue = daysUntilDue; return this; }
        public FilingAlertBuilder acknowledged(boolean acknowledged) { this.acknowledged = acknowledged; return this; }
        public FilingAlertBuilder tenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

        public FilingAlert build() {
            FilingAlert a = new FilingAlert();
            a.id = this.id;
            a.obligationId = this.obligationId;
            a.alertDate = this.alertDate;
            a.daysUntilDue = this.daysUntilDue;
            a.acknowledged = this.acknowledged;
            a.tenantId = this.tenantId;
            return a;
        }
    }
}
