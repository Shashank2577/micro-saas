package com.ecosystemmap.repository;

import com.ecosystemmap.domain.DataFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DataFlowRepository extends JpaRepository<DataFlow, UUID> {
    List<DataFlow> findByTenantId(UUID tenantId);
    List<DataFlow> findByTenantIdAndSourceAppId(UUID tenantId, UUID sourceAppId);
}
