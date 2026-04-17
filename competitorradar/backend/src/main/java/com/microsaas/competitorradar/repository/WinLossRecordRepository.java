package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.WinLossRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WinLossRecordRepository extends JpaRepository<WinLossRecord, UUID> {
    List<WinLossRecord> findByTenantIdOrderByDateDesc(UUID tenantId);
    List<WinLossRecord> findByCompetitorIdAndTenantIdOrderByDateDesc(UUID competitorId, UUID tenantId);
}
