package com.microsaas.realestateitel.repository;

import com.microsaas.realestateitel.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {
    List<Property> findByTenantId(UUID tenantId);
    Optional<Property> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Property> findByTenantIdAndStatus(UUID tenantId, String status);
}
