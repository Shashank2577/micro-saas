package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.ChannelMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChannelMetricRepository extends JpaRepository<ChannelMetric, UUID> {
    List<ChannelMetric> findByTenantId(UUID tenantId);
    Optional<ChannelMetric> findByIdAndTenantId(UUID id, UUID tenantId);
}
