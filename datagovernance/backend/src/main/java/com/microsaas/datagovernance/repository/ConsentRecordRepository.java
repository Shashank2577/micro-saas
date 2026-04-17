package com.microsaas.datagovernance.repository;

import com.microsaas.datagovernance.model.ConsentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ConsentRecordRepository extends JpaRepository<ConsentRecord, UUID> {
    List<ConsentRecord> findByTenantIdAndUserEmail(UUID tenantId, String userEmail);
    List<ConsentRecord> findByTenantId(UUID tenantId);
}
