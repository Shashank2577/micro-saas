package com.microsaas.dataprivacyai.repository;

import com.microsaas.dataprivacyai.domain.PrivacyRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrivacyRiskRepository extends JpaRepository<PrivacyRisk, UUID> {
    List<PrivacyRisk> findByTenantId(UUID tenantId);
}
