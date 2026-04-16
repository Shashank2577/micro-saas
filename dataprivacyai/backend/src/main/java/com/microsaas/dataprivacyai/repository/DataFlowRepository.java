package com.microsaas.dataprivacyai.repository;

import com.microsaas.dataprivacyai.domain.DataFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataFlowRepository extends JpaRepository<DataFlow, UUID> {
    List<DataFlow> findByTenantId(UUID tenantId);
}
