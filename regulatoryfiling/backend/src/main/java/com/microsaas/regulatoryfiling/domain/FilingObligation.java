package com.microsaas.regulatoryfiling.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "filing_obligations")
public class FilingObligation {
    
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String jurisdiction;

    @Column(name = "filing_type", nullable = false)
    private String filingType;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_pattern", nullable = false)
    private RecurrencePattern recurrencePattern;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FilingStatus status;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public FilingObligation() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getJurisdiction() { return jurisdiction; }
    public void setJurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; }

    public String getFilingType() { return filingType; }
    public void setFilingType(String filingType) { this.filingType = filingType; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public RecurrencePattern getRecurrencePattern() { return recurrencePattern; }
    public void setRecurrencePattern(RecurrencePattern recurrencePattern) { this.recurrencePattern = recurrencePattern; }

    public FilingStatus getStatus() { return status; }
    public void setStatus(FilingStatus status) { this.status = status; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public static FilingObligationBuilder builder() {
        return new FilingObligationBuilder();
    }

    public static class FilingObligationBuilder {
        private UUID id;
        private String name;
        private String jurisdiction;
        private String filingType;
        private LocalDate dueDate;
        private RecurrencePattern recurrencePattern;
        private FilingStatus status;
        private UUID tenantId;

        public FilingObligationBuilder id(UUID id) { this.id = id; return this; }
        public FilingObligationBuilder name(String name) { this.name = name; return this; }
        public FilingObligationBuilder jurisdiction(String jurisdiction) { this.jurisdiction = jurisdiction; return this; }
        public FilingObligationBuilder filingType(String filingType) { this.filingType = filingType; return this; }
        public FilingObligationBuilder dueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
        public FilingObligationBuilder recurrencePattern(RecurrencePattern recurrencePattern) { this.recurrencePattern = recurrencePattern; return this; }
        public FilingObligationBuilder status(FilingStatus status) { this.status = status; return this; }
        public FilingObligationBuilder tenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

        public FilingObligation build() {
            FilingObligation o = new FilingObligation();
            o.id = this.id;
            o.name = this.name;
            o.jurisdiction = this.jurisdiction;
            o.filingType = this.filingType;
            o.dueDate = this.dueDate;
            o.recurrencePattern = this.recurrencePattern;
            o.status = this.status;
            o.tenantId = this.tenantId;
            return o;
        }
    }
}
