package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findByTenantId(String tenantId);
    Optional<Incident> findByIdAndTenantId(UUID id, String tenantId);
}
