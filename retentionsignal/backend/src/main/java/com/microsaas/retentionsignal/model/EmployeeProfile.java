package com.microsaas.retentionsignal.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "employee_profiles")
@Data
public class EmployeeProfile {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;
    private String role;
    private String department;
    private int tenureMonths;

    @Enumerated(EnumType.STRING)
    private CompLevel compLevel;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
}
