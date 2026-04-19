package com.microsaas.cashflowai.repository;

import com.microsaas.cashflowai.model.FundingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FundingEventRepository extends JpaRepository<FundingEvent, UUID> {
    List<FundingEvent> findByTenantId(UUID tenantId);
    Optional<FundingEvent> findByIdAndTenantId(UUID id, UUID tenantId);
}
