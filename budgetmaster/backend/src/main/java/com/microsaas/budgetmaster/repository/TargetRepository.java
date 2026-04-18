package com.microsaas.budgetmaster.repository;

import com.microsaas.budgetmaster.domain.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface TargetRepository extends JpaRepository<Target, UUID> {
    List<Target> findAllByTenantId(UUID tenantId);
    Optional<Target> findByIdAndTenantId(UUID id, UUID tenantId);
}
