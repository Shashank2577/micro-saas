package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.WinningVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WinningVariantRepository extends JpaRepository<WinningVariant, UUID> {
    List<WinningVariant> findByTenantId(UUID tenantId);
    Optional<WinningVariant> findByIdAndTenantId(UUID id, UUID tenantId);
}
