package com.microsaas.datalineagetracker.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.datalineagetracker.dto.AssetDto;
import com.microsaas.datalineagetracker.entity.DataAsset;
import com.microsaas.datalineagetracker.repository.DataAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataAssetService {
    private final DataAssetRepository repository;

    @Transactional(readOnly = true)
    public List<DataAsset> getAllAssets() {
        return repository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public DataAsset getAssetById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Asset not found"));
    }

    @Transactional
    public DataAsset createAsset(AssetDto dto) {
        DataAsset asset = new DataAsset();
        asset.setTenantId(TenantContext.require());
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setSourceSystem(dto.getSourceSystem());
        asset.setOwnerId(dto.getOwnerId());
        asset.setStewardId(dto.getStewardId());
        asset.setClassification(dto.getClassification());
        asset.setDescription(dto.getDescription());
        asset.setRetentionDays(dto.getRetentionDays());
        return repository.save(asset);
    }

    @Transactional
    public DataAsset updateAsset(UUID id, AssetDto dto) {
        DataAsset asset = getAssetById(id);
        asset.setName(dto.getName());
        asset.setType(dto.getType());
        asset.setSourceSystem(dto.getSourceSystem());
        asset.setOwnerId(dto.getOwnerId());
        asset.setStewardId(dto.getStewardId());
        asset.setClassification(dto.getClassification());
        asset.setDescription(dto.getDescription());
        asset.setRetentionDays(dto.getRetentionDays());
        return repository.save(asset);
    }

    @Transactional
    public void deleteAsset(UUID id) {
        DataAsset asset = getAssetById(id);
        repository.delete(asset);
    }
}
