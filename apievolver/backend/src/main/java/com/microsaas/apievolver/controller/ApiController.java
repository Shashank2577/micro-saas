package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.model.ApiSpec;
import com.microsaas.apievolver.model.ApiChange;
import com.microsaas.apievolver.model.ApiDependency;
import com.microsaas.apievolver.service.ApiSpecService;
import com.microsaas.apievolver.service.ConsumerRegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {
    private final ApiSpecService apiSpecService;
    private final ConsumerRegistryService registryService;

    @PostMapping("/specs")
    public ResponseEntity<ApiSpec> uploadSpec(@RequestBody Map<String, String> request) {
        String serviceId = request.get("serviceId");
        String version = request.get("version");
        ApiSpec.SpecType type = ApiSpec.SpecType.valueOf(request.getOrDefault("specType", "OPENAPI").toUpperCase());
        String content = request.get("specContent");

        ApiSpec spec = apiSpecService.uploadSpec(serviceId, version, type, content);
        return ResponseEntity.ok(spec);
    }

    @GetMapping("/specs/{serviceId}")
    public ResponseEntity<List<ApiSpec>> getSpecs(@PathVariable String serviceId) {
        return ResponseEntity.ok(apiSpecService.getSpecsForService(serviceId));
    }

    @GetMapping("/specs/{specId}/changes")
    public ResponseEntity<List<ApiChange>> getChanges(@PathVariable Long specId) {
        return ResponseEntity.ok(apiSpecService.getChangesForSpec(specId));
    }

    @PostMapping("/consumers")
    public ResponseEntity<ApiDependency> registerConsumer(@RequestBody Map<String, Object> request) {
        String consumer = (String) request.get("consumerServiceId");
        String provider = (String) request.get("providerServiceId");
        List<String> endpoints = (List<String>) request.get("usedEndpoints");
        Boolean sensitive = (Boolean) request.getOrDefault("sensitiveToBreakingChanges", true);

        ApiDependency dep = registryService.registerDependency(consumer, provider, endpoints, sensitive);
        return ResponseEntity.ok(dep);
    }

    @GetMapping("/providers/{providerId}/consumers")
    public ResponseEntity<List<ApiDependency>> getConsumers(@PathVariable String providerId) {
        return ResponseEntity.ok(registryService.getConsumersOf(providerId));
    }
}
