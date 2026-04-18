package com.microsaas.dataunification.repository;

import com.microsaas.dataunification.model.SchemaMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface SchemaMappingRepository extends JpaRepository<SchemaMapping, UUID> {
    List<SchemaMapping> findByTenantId(UUID tenantId);
}
