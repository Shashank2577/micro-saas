package com.microsaas.investtracker.repository;

import com.microsaas.investtracker.entity.TaxLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaxLotRepository extends JpaRepository<TaxLot, UUID> {
    List<TaxLot> findByTenantIdAndHoldingId(UUID tenantId, UUID holdingId);
}
