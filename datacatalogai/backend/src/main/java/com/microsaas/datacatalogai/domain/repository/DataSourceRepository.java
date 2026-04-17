package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, UUID> {
    List<DataSource> findAllByTenantId(String tenantId);
    Optional<DataSource> findByIdAndTenantId(UUID id, String tenantId);
}
