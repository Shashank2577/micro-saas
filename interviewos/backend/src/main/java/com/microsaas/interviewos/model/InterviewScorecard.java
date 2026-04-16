package com.microsaas.interviewos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interview_scorecard", schema = "interviewos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewScorecard {
    @Id
    private UUID id;
    
    private UUID guideId;
    
    private UUID candidateId;
    
    private UUID interviewerId;
    
    private String scores;
    
    private int overallScore;
    
    private String notes;
    
    private UUID tenantId;
    
    private LocalDateTime submittedAt;
}
