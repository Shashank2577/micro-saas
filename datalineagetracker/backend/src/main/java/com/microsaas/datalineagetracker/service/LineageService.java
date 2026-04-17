package com.microsaas.datalineagetracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datalineagetracker.dto.LineageLinkDto;
import com.microsaas.datalineagetracker.entity.DataAsset;
import com.microsaas.datalineagetracker.entity.DataLineageLink;
import com.microsaas.datalineagetracker.repository.DataLineageLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LineageService {
    private final DataLineageLinkRepository linkRepository;
    private final DataAssetService assetService;
    private final JdbcTemplate jdbcTemplate;
    private static final String PGMQ_QUEUE_NAME = "lineage_computation";

    @Transactional
    public void createLinkAsync(LineageLinkDto dto) {
        String payload = String.format("{\"tenantId\":\"%s\", \"sourceId\":\"%s\", \"targetId\":\"%s\", \"logic\":\"%s\"}",
                TenantContext.require(), dto.getSourceAssetId(), dto.getTargetAssetId(), dto.getTransformationLogic() != null ? dto.getTransformationLogic().replace("\"", "\\\"") : "");
        
        try {
            // Attempt to use pgmq if extension exists
            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS pgmq;");
            jdbcTemplate.execute("SELECT pgmq.create('" + PGMQ_QUEUE_NAME + "');");
            jdbcTemplate.update("SELECT pgmq.send(?, ?::jsonb)", PGMQ_QUEUE_NAME, payload);
            log.info("Sent lineage link computation task to pgmq: {}", payload);
        } catch (Exception e) {
            log.warn("Failed to send to pgmq, falling back to sync creation: {}", e.getMessage());
            // Fallback for autonomous tests/mocking
            createLink(dto);
        }
    }

    @Transactional
    public DataLineageLink createLink(LineageLinkDto dto) {
        DataAsset source = assetService.getAssetById(dto.getSourceAssetId());
        DataAsset target = assetService.getAssetById(dto.getTargetAssetId());

        DataLineageLink link = new DataLineageLink();
        link.setTenantId(TenantContext.require());
        link.setSourceAsset(source);
        link.setTargetAsset(target);
        link.setTransformationLogic(dto.getTransformationLogic());
        return linkRepository.save(link);
    }

    @Transactional(readOnly = true)
    public List<DataLineageLink> getUpstream(UUID assetId) {
        // Just gets immediate upstream for simplicity
        return linkRepository.findAllByTenantIdAndTargetAssetId(TenantContext.require(), assetId);
    }

    @Transactional(readOnly = true)
    public List<DataLineageLink> getDownstream(UUID assetId) {
        // Just gets immediate downstream for simplicity
        return linkRepository.findAllByTenantIdAndSourceAssetId(TenantContext.require(), assetId);
    }

    @Transactional(readOnly = true)
    public List<DataLineageLink> getAllLinks() {
        return linkRepository.findAllByTenantId(TenantContext.require());
    }
}
