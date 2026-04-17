package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.ContentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentPerformanceRepository extends JpaRepository<ContentPerformance, UUID> {
    List<ContentPerformance> findByTenantIdAndChannelId(UUID tenantId, UUID channelId);
    List<ContentPerformance> findByTenantId(UUID tenantId);
}
