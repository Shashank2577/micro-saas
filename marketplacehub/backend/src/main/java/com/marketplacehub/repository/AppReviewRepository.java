package com.marketplacehub.repository;

import com.marketplacehub.model.AppReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppReviewRepository extends JpaRepository<AppReview, UUID> {
    List<AppReview> findByTenantIdAndAppId(UUID tenantId, UUID appId);
    List<AppReview> findByTenantIdAndAppIdAndStatus(UUID tenantId, UUID appId, String status);
}
