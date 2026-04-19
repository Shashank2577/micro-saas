package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, UUID> {
    List<PurchaseRequest> findByTenantId(UUID tenantId);
    Optional<PurchaseRequest> findByIdAndTenantId(UUID id, UUID tenantId);
}
