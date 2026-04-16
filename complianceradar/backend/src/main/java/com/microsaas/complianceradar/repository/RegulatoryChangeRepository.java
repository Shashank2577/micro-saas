package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.RegulatoryChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegulatoryChangeRepository extends JpaRepository<RegulatoryChange, UUID> {
    List<RegulatoryChange> findByTenantId(UUID tenantId);
    Optional<RegulatoryChange> findByIdAndTenantId(UUID id, UUID tenantId);
}
