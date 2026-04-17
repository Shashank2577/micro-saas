package com.microsaas.billingai.repository;

import com.microsaas.billingai.model.Invoice;
import com.microsaas.billingai.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findByTenantId(UUID tenantId);
    List<Invoice> findBySubscriptionIdAndTenantId(UUID subscriptionId, UUID tenantId);
    List<Invoice> findByStatusAndTenantId(InvoiceStatus status, UUID tenantId);
}
