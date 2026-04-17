package com.microsaas.dataroomai.repository;

import com.microsaas.dataroomai.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByRoomIdAndTenantId(UUID roomId, UUID tenantId);
    Optional<Document> findByIdAndTenantId(UUID id, UUID tenantId);
}
