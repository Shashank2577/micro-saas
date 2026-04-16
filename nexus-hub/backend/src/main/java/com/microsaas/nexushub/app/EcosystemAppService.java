package com.microsaas.nexushub.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EcosystemAppService {

    private final EcosystemAppRepository appRepository;

    @Transactional
    public EcosystemApp register(UUID tenantId, AppRegistrationRequest request) {
        EcosystemApp app = appRepository.findByTenantIdAndName(tenantId, request.name())
                .orElseGet(EcosystemApp::new);

        app.setTenantId(tenantId);
        app.setName(request.name());
        app.setDisplayName(request.displayName());
        app.setBaseUrl(request.baseUrl());
        app.setManifest(request.manifest());
        app.setStatus(EcosystemApp.AppStatus.ACTIVE);
        app.setLastHeartbeatAt(Instant.now());

        return appRepository.save(app);
    }

    public List<EcosystemApp> listApps(UUID tenantId) {
        return appRepository.findByTenantId(tenantId);
    }

    public List<EcosystemApp> listActiveApps(UUID tenantId) {
        return appRepository.findByTenantIdAndStatus(tenantId, EcosystemApp.AppStatus.ACTIVE);
    }

    @Transactional
    public void deregister(UUID tenantId, UUID appId) {
        appRepository.findById(appId)
                .filter(a -> a.getTenantId().equals(tenantId))
                .ifPresent(a -> {
                    a.setStatus(EcosystemApp.AppStatus.INACTIVE);
                    appRepository.save(a);
                });
    }

    @Transactional
    public void heartbeat(UUID tenantId, String appName) {
        appRepository.findByTenantIdAndName(tenantId, appName)
                .ifPresent(a -> {
                    a.setLastHeartbeatAt(Instant.now());
                    appRepository.save(a);
                });
    }
}
