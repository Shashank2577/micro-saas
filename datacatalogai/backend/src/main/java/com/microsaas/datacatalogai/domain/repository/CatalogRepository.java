package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, UUID> {
    List<Catalog> findAllByTenantId(String tenantId);
    Optional<Catalog> findByIdAndTenantId(UUID id, String tenantId);
}
