package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.CustomerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerReviewRepository extends JpaRepository<CustomerReview, UUID> {
    List<CustomerReview> findByCompetitorIdAndTenantIdOrderByPostedAtDesc(UUID competitorId, UUID tenantId);
}
