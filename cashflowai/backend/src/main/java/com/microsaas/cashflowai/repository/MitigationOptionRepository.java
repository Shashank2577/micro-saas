package com.microsaas.cashflowai.repository;

import com.microsaas.cashflowai.model.MitigationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MitigationOptionRepository extends JpaRepository<MitigationOption, UUID> {
    List<MitigationOption> findByTenantId(UUID tenantId);
    Optional<MitigationOption> findByIdAndTenantId(UUID id, UUID tenantId);
}
