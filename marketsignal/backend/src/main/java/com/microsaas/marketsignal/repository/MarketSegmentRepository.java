package com.microsaas.marketsignal.repository;

import com.microsaas.marketsignal.domain.entity.MarketSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketSegmentRepository extends JpaRepository<MarketSegment, UUID> {
    List<MarketSegment> findByTenantId(UUID tenantId);
}
