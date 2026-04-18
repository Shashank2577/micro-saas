package com.microsaas.legalresearch.repository;

import com.microsaas.legalresearch.domain.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, UUID> {
    List<ReviewComment> findByTenantId(UUID tenantId);
    Optional<ReviewComment> findByIdAndTenantId(UUID id, UUID tenantId);
}
