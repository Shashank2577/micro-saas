package com.microsaas.interviewos.repository;

import com.microsaas.interviewos.model.EvaluatorConsistency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EvaluatorConsistencyRepository extends JpaRepository<EvaluatorConsistency, UUID> {
    Optional<EvaluatorConsistency> findByInterviewerIdAndTenantId(UUID interviewerId, UUID tenantId);
}
