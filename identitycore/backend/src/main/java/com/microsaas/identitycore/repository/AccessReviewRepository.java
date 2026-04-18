package com.microsaas.identitycore.repository;

import com.microsaas.identitycore.model.AccessReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessReviewRepository extends JpaRepository<AccessReview, UUID> {
    List<AccessReview> findByTenantId(UUID tenantId);
    Optional<AccessReview> findByIdAndTenantId(UUID id, UUID tenantId);
}
