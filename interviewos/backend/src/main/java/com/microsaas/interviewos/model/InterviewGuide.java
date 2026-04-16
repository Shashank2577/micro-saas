package com.microsaas.interviewos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "interview_guide", schema = "interviewos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewGuide {
    @Id
    private UUID id;
    
    private String roleTitle;
    
    @Enumerated(EnumType.STRING)
    private InterviewType interviewType;
    
    private String questions;
    
    private UUID tenantId;
    
    private LocalDateTime createdAt;
}
