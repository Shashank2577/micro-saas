package com.microsaas.compbenchmark.repositories;

import com.microsaas.compbenchmark.model.ApprovalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalRecordRepository extends JpaRepository<ApprovalRecord, UUID> {
    List<ApprovalRecord> findByTenantId(UUID tenantId);
    Optional<ApprovalRecord> findByIdAndTenantId(UUID id, UUID tenantId);
}
