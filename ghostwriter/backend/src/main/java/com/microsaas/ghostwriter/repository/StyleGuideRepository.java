package com.microsaas.ghostwriter.repository;

import com.microsaas.ghostwriter.model.StyleGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StyleGuideRepository extends JpaRepository<StyleGuide, UUID> {
    List<StyleGuide> findByTenantId(String tenantId);
    Optional<StyleGuide> findByIdAndTenantId(UUID id, String tenantId);
}
