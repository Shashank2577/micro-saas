package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.AiInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AiInsightRepository extends JpaRepository<AiInsight, UUID> {
    List<AiInsight> findByTenantIdAndEcosystemId(String tenantId, UUID ecosystemId);
    Optional<AiInsight> findByIdAndTenantId(UUID id, String tenantId);
}
