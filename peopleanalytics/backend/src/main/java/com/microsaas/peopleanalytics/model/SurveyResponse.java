package com.microsaas.peopleanalytics.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "survey_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private PulseSurvey survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Integer score;

    @Column(name = "feedback")
    private byte[] feedback; // Encrypted

    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private OffsetDateTime submittedAt;
}
