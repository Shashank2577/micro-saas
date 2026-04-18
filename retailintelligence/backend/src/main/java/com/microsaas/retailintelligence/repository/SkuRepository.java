package com.microsaas.retailintelligence.repository;

import com.microsaas.retailintelligence.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkuRepository extends JpaRepository<Sku, UUID> {
    List<Sku> findByTenantId(UUID tenantId);
    Optional<Sku> findByIdAndTenantId(UUID id, UUID tenantId);
}
