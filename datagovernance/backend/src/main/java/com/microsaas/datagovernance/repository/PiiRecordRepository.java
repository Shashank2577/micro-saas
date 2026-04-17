package com.microsaas.datagovernance.repository;

import com.microsaas.datagovernance.model.PiiRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PiiRecordRepository extends JpaRepository<PiiRecord, UUID> {
    List<PiiRecord> findByTenantId(UUID tenantId);
}
