package com.microsaas.documentintelligence.repository;

import com.microsaas.documentintelligence.model.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, UUID> {
    // Basic native query for vector similarity search assuming pgvector
    @Query(value = "SELECT * FROM document_chunks WHERE tenant_id = :tenantId ORDER BY embedding <-> cast(:embedding as vector) LIMIT :limit", nativeQuery = true)
    List<DocumentChunk> findSimilarChunks(@Param("tenantId") UUID tenantId, @Param("embedding") String embedding, @Param("limit") int limit);
}
