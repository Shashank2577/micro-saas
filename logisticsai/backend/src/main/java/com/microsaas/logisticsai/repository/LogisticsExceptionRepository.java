package com.microsaas.logisticsai.repository;

import com.microsaas.logisticsai.domain.LogisticsException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface LogisticsExceptionRepository extends JpaRepository<LogisticsException, UUID> {
    List<LogisticsException> findByTenantId(UUID tenantId);
    Optional<LogisticsException> findByIdAndTenantId(UUID id, UUID tenantId);
}
