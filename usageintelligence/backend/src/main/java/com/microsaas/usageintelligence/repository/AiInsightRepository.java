package com.microsaas.usageintelligence.repository;

import com.microsaas.usageintelligence.domain.AiInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AiInsightRepository extends JpaRepository<AiInsight, UUID> {
    List<AiInsight> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
}
