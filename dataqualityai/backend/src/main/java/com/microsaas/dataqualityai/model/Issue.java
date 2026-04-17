package com.microsaas.dataqualityai.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dq_issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String testRunId;

    private String severity; // HIGH, MEDIUM, LOW

    private String description;

    @Column(columnDefinition = "TEXT")
    private String rootCauseHypothesis;

    private LocalDateTime ownerNotifiedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
