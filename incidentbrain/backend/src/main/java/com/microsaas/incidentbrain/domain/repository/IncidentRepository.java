package com.microsaas.incidentbrain.domain.repository;

import com.microsaas.incidentbrain.domain.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, String> {
    List<Incident> findByTenantId(String tenantId);
    Optional<Incident> findByIdAndTenantId(String id, String tenantId);
}
