package com.microsaas.identitycore.repository;

import com.microsaas.identitycore.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, UUID> {
    List<Anomaly> findByTenantId(UUID tenantId);
    Optional<Anomaly> findByIdAndTenantId(UUID id, UUID tenantId);
}
