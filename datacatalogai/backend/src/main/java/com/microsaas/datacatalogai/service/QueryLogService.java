package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.QueryLog;
import com.microsaas.datacatalogai.domain.model.Relationship;
import com.microsaas.datacatalogai.domain.repository.QueryLogRepository;
import com.microsaas.datacatalogai.domain.repository.RelationshipRepository;
import com.microsaas.datacatalogai.domain.repository.AssetRepository;
import com.microsaas.datacatalogai.domain.model.Asset;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class QueryLogService {
    private final QueryLogRepository queryLogRepository;
    private final RelationshipRepository relationshipRepository;
    private final AssetRepository assetRepository;

    public List<QueryLog> getQueries(String tenantId, UUID assetId) {
        return queryLogRepository.findAllByTenantIdAndAssetId(tenantId, assetId);
    }

    @Transactional
    public void ingestLogs(String tenantId, List<QueryLog> logs) {
        for (QueryLog log : logs) {
            log.setTenantId(tenantId);
            queryLogRepository.save(log);
        }
    }

    @Transactional
    public void inferLineageFromQuery(String tenantId, String querySql, UUID targetAssetId) {
        if (querySql.toUpperCase().contains("INSERT INTO") && querySql.toUpperCase().contains("SELECT")) {
            Pattern fromPattern = Pattern.compile("FROM\\s+([a-zA-Z0-9_,\\s]+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = fromPattern.matcher(querySql);
            if (matcher.find()) {
                String fromClause = matcher.group(1);
                String[] sources = fromClause.split(",");
                for (String source : sources) {
                    String srcFqn = source.trim();
                    assetRepository.findAllByTenantId(tenantId).stream()
                        .filter(a -> a.getFqn().equalsIgnoreCase(srcFqn))
                        .findFirst()
                        .ifPresent(srcAsset -> {
                            Relationship rel = new Relationship();
                            rel.setTenantId(tenantId);
                            rel.setFromAssetId(srcAsset.getId());
                            rel.setToAssetId(targetAssetId);
                            rel.setRelType("LINEAGE");
                            rel.setConfidence(0.9);
                            rel.setCreatedAt(Instant.now());
                            relationshipRepository.save(rel);
                        });
                }
            }
        }
    }
}
