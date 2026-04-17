package com.microsaas.wealthplan.repository;

import com.microsaas.wealthplan.entity.AllocationRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AllocationRecommendationRepository extends JpaRepository<AllocationRecommendation, UUID> {
    List<AllocationRecommendation> findByTenantIdOrderByCreatedAtDesc(String tenantId);
}
