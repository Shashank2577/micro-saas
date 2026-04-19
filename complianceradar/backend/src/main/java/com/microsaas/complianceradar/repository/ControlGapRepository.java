package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.ControlGap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ControlGapRepository extends JpaRepository<ControlGap, UUID> {
    List<ControlGap> findAllByTenantId(UUID tenantId);
    Optional<ControlGap> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByIdAndTenantId(UUID id, UUID tenantId);
}
