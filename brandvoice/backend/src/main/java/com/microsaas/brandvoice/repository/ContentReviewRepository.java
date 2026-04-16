package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.model.ContentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentReviewRepository extends JpaRepository<ContentReview, UUID> {
    List<ContentReview> findByTenantIdAndBrandProfileId(UUID tenantId, UUID brandProfileId);
}
