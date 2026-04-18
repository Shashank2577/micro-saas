package com.microsaas.realestateitel.repository;

import com.microsaas.realestateitel.domain.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, UUID> {
    List<Lease> findByTenantIdAndPropertyId(UUID tenantId, UUID propertyId);
    Optional<Lease> findByIdAndTenantId(UUID id, UUID tenantId);
}
