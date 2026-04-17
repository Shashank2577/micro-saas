package com.microsaas.knowledgevault.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "knowledge_queries")
public class KnowledgeQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String tenantId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String queryText;

    @Column(columnDefinition = "TEXT")
    private String generatedAnswer;

    @Column(nullable = false)
    private int frequency;

    @Column(nullable = false)
    private Instant lastAskedAt;

    @Column(nullable = false)
    private Instant createdAt;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }

    public String getGeneratedAnswer() { return generatedAnswer; }
    public void setGeneratedAnswer(String generatedAnswer) { this.generatedAnswer = generatedAnswer; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public Instant getLastAskedAt() { return lastAskedAt; }
    public void setLastAskedAt(Instant lastAskedAt) { this.lastAskedAt = lastAskedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        lastAskedAt = Instant.now();
        if (frequency == 0) {
            frequency = 1;
        }
    }
}
