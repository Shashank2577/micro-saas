package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.Relationship;
import com.microsaas.datacatalogai.domain.repository.RelationshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RelationshipService {
    private final RelationshipRepository relationshipRepository;

    public List<Relationship> getLineage(String tenantId, UUID assetId) {
        List<Relationship> lineage = new ArrayList<>();
        lineage.addAll(relationshipRepository.findAllByTenantIdAndFromAssetId(tenantId, assetId));
        lineage.addAll(relationshipRepository.findAllByTenantIdAndToAssetId(tenantId, assetId));
        return lineage;
    }

    @Transactional
    public Relationship createRelationship(String tenantId, Relationship relationship) {
        relationship.setTenantId(tenantId);
        return relationshipRepository.save(relationship);
    }
}
