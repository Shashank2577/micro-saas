package com.microsaas.apigatekeeper.repository;

import com.microsaas.apigatekeeper.entity.AnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsSnapshotRepository extends JpaRepository<AnalyticsSnapshot, UUID> {
    List<AnalyticsSnapshot> findByTenantId(String tenantId);
}
