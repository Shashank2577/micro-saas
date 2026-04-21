package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.DealRiskSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealRiskSignalRepository extends JpaRepository<DealRiskSignal, UUID> {
    List<DealRiskSignal> findByTenantIdAndDealId(UUID tenantId, UUID dealId);
}
