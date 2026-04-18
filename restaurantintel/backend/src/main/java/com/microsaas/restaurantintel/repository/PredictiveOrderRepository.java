package com.microsaas.restaurantintel.repository;

import com.microsaas.restaurantintel.domain.PredictiveOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PredictiveOrderRepository extends JpaRepository<PredictiveOrder, UUID> {
    List<PredictiveOrder> findByTenantId(UUID tenantId);
}
