package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.AudienceSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AudienceSegmentRepository extends JpaRepository<AudienceSegment, UUID> {
    List<AudienceSegment> findByTenantId(UUID tenantId);
    Optional<AudienceSegment> findByIdAndTenantId(UUID id, UUID tenantId);
}
