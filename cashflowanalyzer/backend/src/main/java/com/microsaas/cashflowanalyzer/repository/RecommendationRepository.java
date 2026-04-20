package com.microsaas.cashflowanalyzer.repository;

import com.microsaas.cashflowanalyzer.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
    List<Recommendation> findByTenantId(String tenantId);
}
