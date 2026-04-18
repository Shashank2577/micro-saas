package com.microsaas.careerpath.repository;

import com.microsaas.careerpath.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findAllByTenantId(String tenantId);
    Optional<Employee> findByIdAndTenantId(UUID id, String tenantId);
    List<Employee> findAllByTenantIdAndManagerId(String tenantId, UUID managerId);
}
