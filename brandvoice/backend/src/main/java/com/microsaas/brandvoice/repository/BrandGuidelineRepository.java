package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.BrandGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BrandGuidelineRepository extends JpaRepository<BrandGuideline, UUID> {
    List<BrandGuideline> findByTenantId(UUID tenantId);
}
