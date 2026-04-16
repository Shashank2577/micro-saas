package com.microsaas.retentionsignal.repository;

import com.microsaas.retentionsignal.model.RetentionSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RetentionSignalRepository extends JpaRepository<RetentionSignal, UUID> {
    List<RetentionSignal> findByEmployeeIdAndTenantId(UUID employeeId, UUID tenantId);
    List<RetentionSignal> findByTenantId(UUID tenantId);
}
