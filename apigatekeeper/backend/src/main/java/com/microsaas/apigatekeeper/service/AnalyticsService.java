package com.microsaas.apigatekeeper.service;

import com.microsaas.apigatekeeper.entity.AnalyticsSnapshot;
import com.microsaas.apigatekeeper.repository.AnalyticsSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsSnapshotRepository repository;

    public List<AnalyticsSnapshot> getTraffic(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
