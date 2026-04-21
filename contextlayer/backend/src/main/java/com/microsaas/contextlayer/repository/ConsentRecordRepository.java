package com.microsaas.contextlayer.repository;

import com.microsaas.contextlayer.domain.ConsentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsentRecordRepository extends JpaRepository<ConsentRecord, UUID> {
    List<ConsentRecord> findByCustomerIdAndTenantId(String customerId, UUID tenantId);
    Optional<ConsentRecord> findByCustomerIdAndTenantIdAndConsentType(String customerId, UUID tenantId, String consentType);
}
