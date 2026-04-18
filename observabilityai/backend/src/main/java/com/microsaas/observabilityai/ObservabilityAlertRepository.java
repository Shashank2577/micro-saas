package com.microsaas.observabilityai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ObservabilityAlertRepository extends JpaRepository<ObservabilityAlert, UUID> {
    List<ObservabilityAlert> findByTenantId(UUID tenantId);
}
