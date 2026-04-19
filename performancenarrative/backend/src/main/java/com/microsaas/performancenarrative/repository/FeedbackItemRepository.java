package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.FeedbackItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackItemRepository extends JpaRepository<FeedbackItem, UUID> {
    List<FeedbackItem> findByTenantId(UUID tenantId);
    Optional<FeedbackItem> findByIdAndTenantId(UUID id, UUID tenantId);
}
