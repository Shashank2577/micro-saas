package com.microsaas.ecosystemmap.repository;

import com.microsaas.ecosystemmap.entity.DataFlowEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DataFlowEventRepository extends JpaRepository<DataFlowEvent, UUID> {
    List<DataFlowEvent> findByTenantIdAndConnectionId(String tenantId, UUID connectionId);
    Optional<DataFlowEvent> findByIdAndTenantId(UUID id, String tenantId);
}
