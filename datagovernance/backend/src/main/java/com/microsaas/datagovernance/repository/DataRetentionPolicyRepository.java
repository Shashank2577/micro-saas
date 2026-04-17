package com.microsaas.datagovernance.repository;

import com.microsaas.datagovernance.model.DataRetentionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DataRetentionPolicyRepository extends JpaRepository<DataRetentionPolicy, UUID> {
    List<DataRetentionPolicy> findByTenantId(UUID tenantId);
}
