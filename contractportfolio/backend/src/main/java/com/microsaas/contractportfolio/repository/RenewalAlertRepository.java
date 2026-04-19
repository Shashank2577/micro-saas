package com.microsaas.contractportfolio.repository;

import com.microsaas.contractportfolio.domain.RenewalAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RenewalAlertRepository extends JpaRepository<RenewalAlert, UUID> {
    List<RenewalAlert> findAllByTenantId(UUID tenantId);
    Optional<RenewalAlert> findByIdAndTenantId(UUID id, UUID tenantId);
}
