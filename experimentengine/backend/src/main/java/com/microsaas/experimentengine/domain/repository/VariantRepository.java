package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {
    List<Variant> findByExperimentId(UUID experimentId);
}
