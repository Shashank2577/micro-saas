package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.SemanticEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SemanticEmbeddingRepository extends JpaRepository<SemanticEmbedding, UUID> {
    Optional<SemanticEmbedding> findByTenantIdAndAssetId(String tenantId, UUID assetId);

    // We will cast the string vector query to pgvector type in native SQL
    @Query(value = "SELECT * FROM semantic_embeddings WHERE tenant_id = :tenantId ORDER BY vector <-> cast(:queryVector as vector) LIMIT :limit", nativeQuery = true)
    List<SemanticEmbedding> findNearestNeighbors(
            @Param("tenantId") String tenantId,
            @Param("queryVector") String queryVector,
            @Param("limit") int limit);
}
