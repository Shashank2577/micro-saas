package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.EcosystemSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EcosystemSnapshotRepository extends JpaRepository<EcosystemSnapshot, UUID> {
    List<EcosystemSnapshot> findByTenantIdAndEcosystemId(String tenantId, UUID ecosystemId);
    Optional<EcosystemSnapshot> findByIdAndTenantId(UUID id, String tenantId);
}
