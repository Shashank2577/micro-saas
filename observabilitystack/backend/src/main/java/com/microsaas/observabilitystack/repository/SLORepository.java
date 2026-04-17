package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.SLO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SLORepository extends JpaRepository<SLO, UUID> {
    List<SLO> findByTenantId(String tenantId);
    Optional<SLO> findByIdAndTenantId(UUID id, String tenantId);
}
