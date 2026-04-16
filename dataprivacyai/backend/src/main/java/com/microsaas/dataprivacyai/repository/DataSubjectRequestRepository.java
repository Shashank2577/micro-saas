package com.microsaas.dataprivacyai.repository;

import com.microsaas.dataprivacyai.domain.DataSubjectRequest;
import com.microsaas.dataprivacyai.domain.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DataSubjectRequestRepository extends JpaRepository<DataSubjectRequest, UUID> {
    List<DataSubjectRequest> findByTenantIdAndStatus(UUID tenantId, RequestStatus status);
    List<DataSubjectRequest> findByTenantIdAndDueDateBeforeAndStatusNot(UUID tenantId, LocalDate dueDate, RequestStatus status);
}
