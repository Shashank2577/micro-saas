package com.microsaas.apimanager.controller;

import com.microsaas.apimanager.model.ApiVersion;
import com.microsaas.apimanager.repository.ApiVersionRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/mock/{projectId}/{versionId}")
@RequiredArgsConstructor
public class MockController {

    private final ApiVersionRepository versionRepository;

    @RequestMapping("/**")
    public ResponseEntity<Map<String, Object>> handleMockRequest(
            @PathVariable UUID projectId,
            @PathVariable UUID versionId,
            HttpServletRequest request) {
        
        Optional<ApiVersion> versionOpt = versionRepository.findByIdAndTenantId(versionId, TenantContext.require().toString());
        if (versionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ApiVersion version = versionOpt.get();
        if (version.getOpenapiSchema() == null) {
            return ResponseEntity.notFound().build();
        }

        // Return a mock response that simulates dynamic schema-based response
        return ResponseEntity.ok(Map.of(
            "message", "Dynamic mock response based on schema: " + version.getVersionString(),
            "method", request.getMethod(),
            "path", request.getRequestURI()
        ));
    }
}
