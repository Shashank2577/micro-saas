package com.microsaas.datagovernance.repository;

import com.microsaas.datagovernance.model.DataSubjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DataSubjectRequestRepository extends JpaRepository<DataSubjectRequest, UUID> {
    List<DataSubjectRequest> findByTenantId(UUID tenantId);
}
