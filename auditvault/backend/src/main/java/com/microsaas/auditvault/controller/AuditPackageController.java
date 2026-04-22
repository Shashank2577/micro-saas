package com.microsaas.auditvault.controller;

import com.microsaas.auditvault.dto.PackageGenerateRequest;
import com.microsaas.auditvault.model.AuditPackage;
import com.microsaas.auditvault.service.AuditPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/packages")
@RequiredArgsConstructor
public class AuditPackageController {

    private final AuditPackageService packageService;

    @PostMapping
    public AuditPackage generatePackage(@RequestBody PackageGenerateRequest request) {
        return packageService.generatePackage(request);
    }

    @GetMapping
    public List<AuditPackage> listPackages() {
        return packageService.listPackages();
    }

    @GetMapping("/{id}")
    public AuditPackage getPackage(@PathVariable UUID id) {
        return packageService.getPackage(id);
    }
}
