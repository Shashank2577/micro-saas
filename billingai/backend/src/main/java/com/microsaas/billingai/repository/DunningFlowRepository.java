package com.microsaas.billingai.repository;

import com.microsaas.billingai.model.DunningFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DunningFlowRepository extends JpaRepository<DunningFlow, UUID> {
    List<DunningFlow> findByTenantId(UUID tenantId);
}
