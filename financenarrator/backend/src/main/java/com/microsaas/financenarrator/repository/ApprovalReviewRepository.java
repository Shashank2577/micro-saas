package com.microsaas.financenarrator.repository;

import com.microsaas.financenarrator.model.ApprovalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalReviewRepository extends JpaRepository<ApprovalReview, UUID> {
    List<ApprovalReview> findByTenantId(UUID tenantId);
    Optional<ApprovalReview> findByIdAndTenantId(UUID id, UUID tenantId);
}
