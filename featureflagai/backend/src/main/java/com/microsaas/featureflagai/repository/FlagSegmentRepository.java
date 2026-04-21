package com.microsaas.featureflagai.repository;

import com.microsaas.featureflagai.domain.FlagSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlagSegmentRepository extends JpaRepository<FlagSegment, UUID> {
    List<FlagSegment> findByFlagIdAndTenantId(UUID flagId, UUID tenantId);
}
