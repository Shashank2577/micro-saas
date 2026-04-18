package com.microsaas.procurebot.repository;

import com.microsaas.procurebot.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    List<PurchaseOrder> findByTenantId(UUID tenantId);
    Optional<PurchaseOrder> findByIdAndTenantId(UUID id, UUID tenantId);
}
