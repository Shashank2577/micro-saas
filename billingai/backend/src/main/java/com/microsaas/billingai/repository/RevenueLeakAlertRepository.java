package com.microsaas.billingai.repository;

import com.microsaas.billingai.model.RevenueLeakAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RevenueLeakAlertRepository extends JpaRepository<RevenueLeakAlert, UUID> {
    List<RevenueLeakAlert> findByTenantId(UUID tenantId);
}
