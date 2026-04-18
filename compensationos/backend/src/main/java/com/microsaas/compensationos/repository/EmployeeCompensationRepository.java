package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.EmployeeCompensation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeCompensationRepository extends JpaRepository<EmployeeCompensation, UUID> {
    List<EmployeeCompensation> findByTenantId(UUID tenantId);
    List<EmployeeCompensation> findByTenantIdAndDepartment(UUID tenantId, String department);
    List<EmployeeCompensation> findByTenantIdAndRole(UUID tenantId, String role);
}
