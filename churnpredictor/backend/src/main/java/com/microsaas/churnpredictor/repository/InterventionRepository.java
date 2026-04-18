package com.microsaas.churnpredictor.repository;

import com.microsaas.churnpredictor.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InterventionRepository extends JpaRepository<Intervention, UUID> {
    List<Intervention> findByTenantId(UUID tenantId);
    Optional<Intervention> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Intervention> findByTenantIdAndCustomerId(UUID tenantId, UUID customerId);
}
