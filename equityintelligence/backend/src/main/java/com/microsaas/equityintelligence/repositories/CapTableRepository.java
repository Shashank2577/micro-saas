package com.microsaas.equityintelligence.repositories;

import com.microsaas.equityintelligence.model.CapTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapTableRepository extends JpaRepository<CapTable, UUID> {
    List<CapTable> findByTenantId(UUID tenantId);
    Optional<CapTable> findByIdAndTenantId(UUID id, UUID tenantId);
}
