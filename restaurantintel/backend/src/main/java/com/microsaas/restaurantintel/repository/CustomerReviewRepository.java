package com.microsaas.restaurantintel.repository;

import com.microsaas.restaurantintel.domain.CustomerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerReviewRepository extends JpaRepository<CustomerReview, UUID> {
    List<CustomerReview> findByTenantId(UUID tenantId);
}
