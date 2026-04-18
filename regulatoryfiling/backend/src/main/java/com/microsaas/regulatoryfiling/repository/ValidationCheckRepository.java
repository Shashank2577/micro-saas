package com.microsaas.regulatoryfiling.repository;

import com.microsaas.regulatoryfiling.domain.ValidationCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ValidationCheckRepository extends JpaRepository<ValidationCheck, UUID> {
    List<ValidationCheck> findByTenantId(UUID tenantId);
    Optional<ValidationCheck> findByIdAndTenantId(UUID id, UUID tenantId);
}
