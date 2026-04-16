package com.microsaas.performancenarrative.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Table(name = "employee_review", schema = "performancenarrative")
@Data
public class EmployeeReview {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID cycleId;
    private UUID employeeId;
    private String employeeName;
    private String role;

    @Enumerated(EnumType.STRING)
    private Rating overallRating;

    @Column(columnDefinition = "TEXT")
    private String draftNarrative;

    @Column(columnDefinition = "TEXT")
    private String finalNarrative;

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
    private UUID tenantId;

    public enum Rating {
        EXCEEDS, MEETS, BELOW
    }

    public enum ReviewStatus {
        DRAFT, IN_REVIEW, FINALIZED
    }
}
