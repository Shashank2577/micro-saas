package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.HiringSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HiringSignalRepository extends JpaRepository<HiringSignal, UUID> {
    List<HiringSignal> findByCompetitorIdAndTenantIdOrderByPostedAtDesc(UUID competitorId, UUID tenantId);
}
