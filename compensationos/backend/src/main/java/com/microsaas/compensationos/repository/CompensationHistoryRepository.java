package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.CompensationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompensationHistoryRepository extends JpaRepository<CompensationHistory, UUID> {
    List<CompensationHistory> findByTenantIdAndEmployeeId(UUID tenantId, UUID employeeId);
}
