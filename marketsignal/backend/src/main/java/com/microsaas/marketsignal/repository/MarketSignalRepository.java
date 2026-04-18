package com.microsaas.marketsignal.repository;

import com.microsaas.marketsignal.domain.entity.MarketSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarketSignalRepository extends JpaRepository<MarketSignal, UUID> {
    List<MarketSignal> findByTenantIdOrderByPublishedAtDesc(UUID tenantId);
}
