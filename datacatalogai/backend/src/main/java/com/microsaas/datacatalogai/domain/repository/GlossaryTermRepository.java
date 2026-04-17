package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.GlossaryTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GlossaryTermRepository extends JpaRepository<GlossaryTerm, UUID> {
    List<GlossaryTerm> findAllByTenantId(String tenantId);
    Optional<GlossaryTerm> findByIdAndTenantId(UUID id, String tenantId);
}
