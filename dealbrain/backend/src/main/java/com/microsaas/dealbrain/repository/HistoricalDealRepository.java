package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.HistoricalDeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricalDealRepository extends JpaRepository<HistoricalDeal, UUID> {
    List<HistoricalDeal> findByTenantId(UUID tenantId);
    List<HistoricalDeal> findByTenantIdAndOutcome(UUID tenantId, String outcome);
}
