package com.microsaas.datacatalogai.domain.repository;

import com.microsaas.datacatalogai.domain.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, UUID> {
    List<Relationship> findAllByTenantIdAndFromAssetId(String tenantId, UUID fromAssetId);
    List<Relationship> findAllByTenantIdAndToAssetId(String tenantId, UUID toAssetId);
}
