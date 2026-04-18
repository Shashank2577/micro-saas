package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.AttritionSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttritionSignalRepository extends JpaRepository<AttritionSignal, UUID> {
    List<AttritionSignal> findByTenantId(UUID tenantId);
    Optional<AttritionSignal> findByIdAndTenantId(UUID id, UUID tenantId);
}
