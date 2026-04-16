package com.microsaas.invoiceprocessor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    List<PurchaseOrder> findByTenantIdAndStatus(UUID tenantId, PoStatus status);
    List<PurchaseOrder> findByTenantIdAndVendorIdAndStatus(UUID tenantId, UUID vendorId, PoStatus status);
}
