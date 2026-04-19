package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, UUID> {
    List<EmployeeReview> findByTenantId(UUID tenantId);
    Optional<EmployeeReview> findByIdAndTenantId(UUID id, UUID tenantId);
}
