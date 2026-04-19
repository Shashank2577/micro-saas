package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.HeadcountMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HeadcountMetricRepository extends JpaRepository<HeadcountMetric, UUID> {
    List<HeadcountMetric> findByTenantId(UUID tenantId);
    Optional<HeadcountMetric> findByIdAndTenantId(UUID id, UUID tenantId);
}
