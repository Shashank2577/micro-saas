package com.microsaas.peopleanalytics.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "employee_number", nullable = false)
    private String employeeNumber;

    @Column(name = "first_name", nullable = false)
    @Convert(converter = PiiConverter.class)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Convert(converter = PiiConverter.class)
    private String lastName;

    @Column(name = "email", nullable = false)
    @Convert(converter = PiiConverter.class)
    private String email;

    @Column(name = "department")
    private String department;

    @Column(name = "role")
    private String role;

    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
