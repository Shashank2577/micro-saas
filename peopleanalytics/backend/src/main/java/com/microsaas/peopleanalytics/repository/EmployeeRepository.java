package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findAllByTenantId(UUID tenantId);
    Optional<Employee> findByIdAndTenantId(UUID id, UUID tenantId);
    Optional<Employee> findByExternalIdAndTenantId(String externalId, UUID tenantId);
}
