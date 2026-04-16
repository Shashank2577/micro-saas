package com.microsaas.regulatoryfiling.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "filing_drafts")
public class FilingDraft {

    @Id
    private UUID id;

    @Column(name = "obligation_id", nullable = false)
    private UUID obligationId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DraftStatus status;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    public FilingDraft() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getObligationId() { return obligationId; }
    public void setObligationId(UUID obligationId) { this.obligationId = obligationId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public DraftStatus getStatus() { return status; }
    public void setStatus(DraftStatus status) { this.status = status; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public static FilingDraftBuilder builder() {
        return new FilingDraftBuilder();
    }

    public static class FilingDraftBuilder {
        private UUID id;
        private UUID obligationId;
        private String content;
        private LocalDateTime generatedAt;
        private DraftStatus status;
        private UUID tenantId;

        public FilingDraftBuilder id(UUID id) { this.id = id; return this; }
        public FilingDraftBuilder obligationId(UUID obligationId) { this.obligationId = obligationId; return this; }
        public FilingDraftBuilder content(String content) { this.content = content; return this; }
        public FilingDraftBuilder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }
        public FilingDraftBuilder status(DraftStatus status) { this.status = status; return this; }
        public FilingDraftBuilder tenantId(UUID tenantId) { this.tenantId = tenantId; return this; }

        public FilingDraft build() {
            FilingDraft d = new FilingDraft();
            d.id = this.id;
            d.obligationId = this.obligationId;
            d.content = this.content;
            d.generatedAt = this.generatedAt;
            d.status = this.status;
            d.tenantId = this.tenantId;
            return d;
        }
    }
}
