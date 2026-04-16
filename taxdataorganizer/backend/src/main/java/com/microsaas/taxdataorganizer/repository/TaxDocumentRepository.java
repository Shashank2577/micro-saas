package com.microsaas.taxdataorganizer.repository;

import com.microsaas.taxdataorganizer.model.TaxDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaxDocumentRepository extends JpaRepository<TaxDocument, UUID> {
    List<TaxDocument> findAllByTaxYearIdAndTenantId(UUID taxYearId, UUID tenantId);
}
