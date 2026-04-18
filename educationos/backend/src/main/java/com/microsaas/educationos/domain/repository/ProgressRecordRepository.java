package com.microsaas.educationos.domain.repository;

import com.microsaas.educationos.domain.entity.ProgressRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressRecordRepository extends JpaRepository<ProgressRecord, UUID> {
    Optional<ProgressRecord> findByLearnerProfileIdAndModuleIdAndTenantId(UUID learnerProfileId, UUID moduleId, UUID tenantId);
    List<ProgressRecord> findByLearnerProfileIdAndTenantId(UUID learnerProfileId, UUID tenantId);
}
