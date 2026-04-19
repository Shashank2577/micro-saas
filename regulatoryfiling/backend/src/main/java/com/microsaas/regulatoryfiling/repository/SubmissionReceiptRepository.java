package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.SubmissionReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubmissionReceiptRepository extends JpaRepository<SubmissionReceipt, UUID> {
    List<SubmissionReceipt> findByTenantId(UUID tenantId);
    Optional<SubmissionReceipt> findByIdAndTenantId(UUID id, UUID tenantId);
}
