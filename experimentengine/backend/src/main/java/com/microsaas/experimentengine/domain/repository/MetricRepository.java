package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MetricRepository extends JpaRepository<Metric, UUID> {
    List<Metric> findByExperimentId(UUID experimentId);
}
