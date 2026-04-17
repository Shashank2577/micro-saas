package com.microsaas.datagovernance.repository;

import com.microsaas.datagovernance.model.DataLineageNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DataLineageNodeRepository extends JpaRepository<DataLineageNode, UUID> {
    List<DataLineageNode> findByTenantIdAndFieldName(UUID tenantId, String fieldName);
}
