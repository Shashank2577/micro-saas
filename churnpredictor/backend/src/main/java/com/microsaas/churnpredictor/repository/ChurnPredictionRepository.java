package com.microsaas.churnpredictor.repository;

import com.microsaas.churnpredictor.entity.ChurnPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChurnPredictionRepository extends JpaRepository<ChurnPrediction, UUID> {
    List<ChurnPrediction> findByTenantId(UUID tenantId);
    List<ChurnPrediction> findByTenantIdAndCustomerIdOrderByPredictedAtDesc(UUID tenantId, UUID customerId);
    Optional<ChurnPrediction> findFirstByTenantIdAndCustomerIdOrderByPredictedAtDesc(UUID tenantId, UUID customerId);

    @Query(value = "SELECT DISTINCT ON (customer_id) * FROM churn_predictions WHERE tenant_id = ?1 ORDER BY customer_id, predicted_at DESC", nativeQuery = true)
    List<ChurnPrediction> findLatestPredictionsByTenantId(UUID tenantId);
}
