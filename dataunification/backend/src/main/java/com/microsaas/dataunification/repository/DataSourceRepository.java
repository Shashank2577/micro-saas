package com.microsaas.dataunification.repository;

import com.microsaas.dataunification.model.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface DataSourceRepository extends JpaRepository<DataSource, UUID> {
    List<DataSource> findByTenantId(UUID tenantId);
}
