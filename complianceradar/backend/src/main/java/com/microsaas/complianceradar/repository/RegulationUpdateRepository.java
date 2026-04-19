package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.RegulationUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegulationUpdateRepository extends JpaRepository<RegulationUpdate, UUID> {
    List<RegulationUpdate> findAllByTenantId(UUID tenantId);
    Optional<RegulationUpdate> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
