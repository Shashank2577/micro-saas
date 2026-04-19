package com.microsaas.hiresignal.repository;

import com.microsaas.hiresignal.model.InterviewStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterviewStageRepository extends JpaRepository<InterviewStage, UUID> {
    List<InterviewStage> findByTenantId(UUID tenantId);
    Optional<InterviewStage> findByIdAndTenantId(UUID id, UUID tenantId);
}
