package com.microsaas.supportintelligence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String status;

    @Column(name = "ai_suggested_response", columnDefinition = "TEXT")
    private String aiSuggestedResponse;

    @Column(name = "escalation_risk_score")
    private Double escalationRiskScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAiSuggestedResponse() { return aiSuggestedResponse; }
    public void setAiSuggestedResponse(String aiSuggestedResponse) { this.aiSuggestedResponse = aiSuggestedResponse; }

    public Double getEscalationRiskScore() { return escalationRiskScore; }
    public void setEscalationRiskScore(Double escalationRiskScore) { this.escalationRiskScore = escalationRiskScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
