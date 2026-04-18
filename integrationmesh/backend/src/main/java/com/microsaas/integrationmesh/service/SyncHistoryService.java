package com.microsaas.integrationmesh.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.integrationmesh.model.SyncHistory;
import com.microsaas.integrationmesh.repository.SyncHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SyncHistoryService {
    private final SyncHistoryRepository syncHistoryRepository;

    public SyncHistoryService(SyncHistoryRepository syncHistoryRepository) {
        this.syncHistoryRepository = syncHistoryRepository;
    }

    public List<SyncHistory> getHistory(UUID integrationId) {
        UUID tenantId = TenantContext.require();
        return syncHistoryRepository.findByIntegrationIdAndTenantIdOrderByStartedAtDesc(integrationId, tenantId);
    }
}
