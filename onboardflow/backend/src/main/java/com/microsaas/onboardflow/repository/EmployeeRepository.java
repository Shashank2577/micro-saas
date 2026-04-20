package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByTenantId(UUID tenantId);
    Optional<Employee> findByIdAndTenantId(UUID id, UUID tenantId);
}
