package com.micro.interviewos.repository;

import com.micro.interviewos.domain.EvaluationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvaluationRecordRepository extends JpaRepository<EvaluationRecord, UUID> {
    List<EvaluationRecord> findByTenantId(UUID tenantId);
    Optional<EvaluationRecord> findByIdAndTenantId(UUID id, UUID tenantId);
}
