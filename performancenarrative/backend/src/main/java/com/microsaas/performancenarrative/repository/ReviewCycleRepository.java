package com.microsaas.performancenarrative.repository;

import com.microsaas.performancenarrative.entity.ReviewCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReviewCycleRepository extends JpaRepository<ReviewCycle, UUID> {
}
