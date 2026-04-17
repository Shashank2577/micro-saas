package com.microsaas.documentintelligence.repository;

import com.microsaas.documentintelligence.model.DocumentAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentAuditTrailRepository extends JpaRepository<DocumentAuditTrail, UUID> {
}
