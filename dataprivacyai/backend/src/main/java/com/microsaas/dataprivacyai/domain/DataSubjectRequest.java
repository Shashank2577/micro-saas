package com.microsaas.dataprivacyai.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "data_subject_requests", schema = "dataprivacyai")
@Getter
@Setter
public class DataSubjectRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    @Column(name = "subject_email")
    private String subjectEmail;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "tenant_id")
    private UUID tenantId;
}
