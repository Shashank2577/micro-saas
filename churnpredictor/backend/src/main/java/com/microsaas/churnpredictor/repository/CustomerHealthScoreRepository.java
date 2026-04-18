package com.microsaas.churnpredictor.repository;

import com.microsaas.churnpredictor.entity.CustomerHealthScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerHealthScoreRepository extends JpaRepository<CustomerHealthScore, UUID> {
    List<CustomerHealthScore> findByTenantIdAndCustomerIdOrderByCalculatedAtDesc(UUID tenantId, UUID customerId);
    Optional<CustomerHealthScore> findFirstByTenantIdAndCustomerIdOrderByCalculatedAtDesc(UUID tenantId, UUID customerId);
}
