package com.microsaas.peopleanalytics.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "first_name")
    private byte[] firstName; // Encrypted

    @Column(name = "last_name")
    private byte[] lastName; // Encrypted

    @Column(name = "email")
    private byte[] email; // Encrypted

    private String department;
    private String role;

    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
