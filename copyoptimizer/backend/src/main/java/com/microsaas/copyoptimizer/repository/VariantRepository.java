package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
    List<Variant> findByTenantId(UUID tenantId);
    Optional<Variant> findByIdAndTenantId(UUID id, UUID tenantId);
}
