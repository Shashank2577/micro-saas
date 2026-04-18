package com.microsaas.realestateitel.repository;

import com.microsaas.realestateitel.domain.Comparable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComparableRepository extends JpaRepository<Comparable, UUID> {
    List<Comparable> findByTenantIdAndSubjectPropertyId(UUID tenantId, UUID subjectPropertyId);
    Optional<Comparable> findByIdAndTenantId(UUID id, UUID tenantId);
    void deleteByTenantIdAndSubjectPropertyId(UUID tenantId, UUID subjectPropertyId);
}
