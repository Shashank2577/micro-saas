package com.microsaas.nexushub.app;

import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/apps")
@RequiredArgsConstructor
public class EcosystemAppController {

    private final EcosystemAppService appService;

    @PostMapping("/register")
    public ResponseEntity<EcosystemApp> register(@RequestBody AppRegistrationRequest request) {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(appService.register(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<EcosystemApp>> list() {
        UUID tenantId = TenantContext.require();
        return ResponseEntity.ok(appService.listApps(tenantId));
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> deregister(@PathVariable UUID appId) {
        UUID tenantId = TenantContext.require();
        appService.deregister(tenantId, appId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/heartbeat/{appName}")
    public ResponseEntity<Void> heartbeat(@PathVariable String appName) {
        UUID tenantId = TenantContext.require();
        appService.heartbeat(tenantId, appName);
        return ResponseEntity.ok().build();
    }
}
