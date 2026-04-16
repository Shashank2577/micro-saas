package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.EmployeeReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EmployeeReviewRepository extends JpaRepository<EmployeeReview, UUID> {
}
