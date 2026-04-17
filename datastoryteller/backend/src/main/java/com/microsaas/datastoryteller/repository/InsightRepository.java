package com.microsaas.datastoryteller.repository;

import com.microsaas.datastoryteller.domain.model.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsightRepository extends JpaRepository<Insight, UUID> {
    List<Insight> findByReportIdAndTenantId(UUID reportId, String tenantId);
}
