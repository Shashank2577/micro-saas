package com.microsaas.customerdiscoveryai.repository;

import com.microsaas.customerdiscoveryai.model.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsightRepository extends JpaRepository<Insight, UUID> {
    List<Insight> findByProjectIdAndTenantId(UUID projectId, UUID tenantId);
    Optional<Insight> findByIdAndTenantId(UUID id, UUID tenantId);
}
