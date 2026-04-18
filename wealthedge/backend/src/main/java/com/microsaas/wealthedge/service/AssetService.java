package com.microsaas.wealthedge.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.wealthedge.domain.Asset;
import com.microsaas.wealthedge.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    @Transactional(readOnly = true)
    public List<Asset> getAllAssets() {
        return assetRepository.findAllByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public Asset getAsset(UUID id) {
        return assetRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));
    }

    @Transactional
    public Asset createAsset(Asset asset) {
        asset.setTenantId(TenantContext.require());
        return assetRepository.save(asset);
    }

    @Transactional
    public Asset updateAsset(UUID id, Asset assetDetails) {
        Asset asset = getAsset(id);
        asset.setName(assetDetails.getName());
        asset.setType(assetDetails.getType());
        asset.setCurrentValue(assetDetails.getCurrentValue());
        asset.setPurchaseValue(assetDetails.getPurchaseValue());
        asset.setPurchaseDate(assetDetails.getPurchaseDate());
        asset.setDescription(assetDetails.getDescription());
        return assetRepository.save(asset);
    }

    @Transactional
    public void deleteAsset(UUID id) {
        Asset asset = getAsset(id);
        assetRepository.delete(asset);
    }
}
