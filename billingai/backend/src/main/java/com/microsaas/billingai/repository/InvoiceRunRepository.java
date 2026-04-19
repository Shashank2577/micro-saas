package com.microsaas.billingai.repository;

import com.microsaas.billingai.model.InvoiceRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRunRepository extends JpaRepository<InvoiceRun, UUID> {
    List<InvoiceRun> findByTenantId(UUID tenantId);
}
