package com.microsaas.marketsignal.repository;

import com.microsaas.marketsignal.domain.entity.MarketPattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketPatternRepository extends JpaRepository<MarketPattern, UUID> {
    List<MarketPattern> findByTenantIdOrderByDetectedAtDesc(UUID tenantId);
}
