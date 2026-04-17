package com.microsaas.wealthplan.repository;

import com.microsaas.wealthplan.entity.EstateDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EstateDocumentRepository extends JpaRepository<EstateDocument, UUID> {
    List<EstateDocument> findByTenantId(String tenantId);
}
