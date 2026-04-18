package com.microsaas.realestateitel.repository;

import com.microsaas.realestateitel.domain.MarketTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarketTrendRepository extends JpaRepository<MarketTrend, UUID> {
    List<MarketTrend> findByTenantIdAndZipCodeOrderByMonthYearDesc(UUID tenantId, String zipCode);
    Optional<MarketTrend> findByIdAndTenantId(UUID id, UUID tenantId);
}
