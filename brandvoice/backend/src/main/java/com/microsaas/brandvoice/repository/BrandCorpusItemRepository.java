package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.model.BrandCorpusItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BrandCorpusItemRepository extends JpaRepository<BrandCorpusItem, UUID> {
    List<BrandCorpusItem> findByTenantIdAndBrandProfileId(UUID tenantId, UUID brandProfileId);
}
