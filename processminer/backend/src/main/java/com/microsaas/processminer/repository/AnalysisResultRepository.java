package com.microsaas.processminer.repository;

import com.microsaas.processminer.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, UUID> {
    List<AnalysisResult> findByTenantIdAndProcessModelId(UUID tenantId, UUID processModelId);
    void deleteByTenantIdAndProcessModelId(UUID tenantId, UUID processModelId);
}
