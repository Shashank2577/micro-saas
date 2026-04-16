package com.microsaas.creatoranalytics.repository;

import com.microsaas.creatoranalytics.model.ContentInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContentInsightRepository extends JpaRepository<ContentInsight, UUID> {
    List<ContentInsight> findByTenantId(UUID tenantId);
}
