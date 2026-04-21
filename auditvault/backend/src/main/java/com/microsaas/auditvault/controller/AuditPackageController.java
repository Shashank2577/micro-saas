package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.dto.PackageGenerateRequest;
import com.microsaas.auditvault.model.AuditPackage;
import com.microsaas.auditvault.service.AuditVaultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class AuditPackageController {

    private final AuditVaultService auditVaultService;

    @PostMapping
    public AuditPackage generatePackage(@RequestBody PackageGenerateRequest request) {
        return auditVaultService.generatePackage(request);
    }

    @GetMapping
    public List<AuditPackage> listPackages() {
        return auditVaultService.listPackages();
    }

    @GetMapping("/{id}")
    public AuditPackage getPackage(@PathVariable UUID id) {
        return auditVaultService.getPackage(id);
    }
}
