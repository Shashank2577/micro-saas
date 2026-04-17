package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByTenantId(UUID tenantId);
    List<Employee> findByTenantIdAndDepartment(UUID tenantId, String department);
    List<Employee> findByTenantIdAndStatus(UUID tenantId, String status);
}
