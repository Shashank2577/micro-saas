package com.microsaas.retentionsignal.repository;

import com.microsaas.retentionsignal.model.FlightRiskScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlightRiskScoreRepository extends JpaRepository<FlightRiskScore, UUID> {
    List<FlightRiskScore> findByTenantIdAndScoreGreaterThanEqual(UUID tenantId, int score);
    List<FlightRiskScore> findByTenantId(UUID tenantId);
}
