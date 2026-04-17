package com.microsaas.datalineagetracker.repository;

import com.microsaas.datalineagetracker.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
    List<Incident> findAllByTenantId(UUID tenantId);
}
