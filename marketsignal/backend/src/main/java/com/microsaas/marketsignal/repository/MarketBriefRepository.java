package com.microsaas.marketsignal.repository;

import com.microsaas.marketsignal.domain.entity.MarketBrief;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketBriefRepository extends JpaRepository<MarketBrief, UUID> {
    List<MarketBrief> findByTenantIdOrderByGeneratedAtDesc(UUID tenantId);
}
