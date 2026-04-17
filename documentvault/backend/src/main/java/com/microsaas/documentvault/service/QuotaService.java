package com.microsaas.documentvault.service;

import com.microsaas.documentvault.exception.OverQuotaException;
import com.microsaas.documentvault.model.StorageQuota;
import com.microsaas.documentvault.repository.StorageQuotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class QuotaService {
    private final StorageQuotaRepository quotaRepository;

    public QuotaService(StorageQuotaRepository quotaRepository) {
        this.quotaRepository = quotaRepository;
    }

    @Transactional
    public void checkQuota(UUID tenantId, long sizeBytes) {
        StorageQuota quota = quotaRepository.findById(tenantId).orElseGet(() -> {
            StorageQuota q = new StorageQuota();
            q.setTenantId(tenantId);
            q.setTotalQuotaBytes(10L * 1024 * 1024 * 1024); // 10 GB default
            q.setUsedBytes(0L);
            return quotaRepository.save(q);
        });

        if (quota.getUsedBytes() + sizeBytes > quota.getTotalQuotaBytes()) {
            throw new OverQuotaException("Storage quota exceeded.");
        }
    }

    @Transactional
    public void updateUsage(UUID tenantId, long deltaBytes) {
        StorageQuota quota = quotaRepository.findById(tenantId).orElseThrow();
        quota.setUsedBytes(quota.getUsedBytes() + deltaBytes);
        quotaRepository.save(quota);
    }
    
    public StorageQuota getQuota(UUID tenantId) {
        return quotaRepository.findById(tenantId).orElseGet(() -> {
            StorageQuota q = new StorageQuota();
            q.setTenantId(tenantId);
            q.setTotalQuotaBytes(10L * 1024 * 1024 * 1024); // 10 GB default
            q.setUsedBytes(0L);
            return quotaRepository.save(q);
        });
    }
}
