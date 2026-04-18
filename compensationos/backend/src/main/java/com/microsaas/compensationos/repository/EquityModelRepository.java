package com.microsaas.compensationos.repository;

import com.microsaas.compensationos.entity.EquityModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EquityModelRepository extends JpaRepository<EquityModel, UUID> {
    List<EquityModel> findByTenantId(UUID tenantId);
}
