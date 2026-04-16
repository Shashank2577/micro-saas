package com.microsaas.interviewos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "evaluator_consistency", schema = "interviewos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluatorConsistency {
    @Id
    private UUID id;
    
    private UUID interviewerId;
    
    private double averageScore;
    
    private int totalEvaluations;
    
    private UUID tenantId;
}
