package com.microsaas.taxoptimizer.controller;

import com.microsaas.taxoptimizer.domain.entity.AccountantCollaboration;
import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import com.microsaas.taxoptimizer.service.AccountantIntegrationService;
import com.microsaas.taxoptimizer.service.TaxProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accountant")
@RequiredArgsConstructor
public class AccountantController {

    private final AccountantIntegrationService accountantIntegrationService;
    private final TaxProfileService taxProfileService;

    @PostMapping("/invite")
    public ResponseEntity<AccountantCollaboration> inviteAccountant(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId,
            @RequestParam String email) {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        return ResponseEntity.ok(accountantIntegrationService.inviteAccountant(tenantId, profile, email));
    }

    @GetMapping("/package")
    public ResponseEntity<byte[]> generateTaxPackage(
            @RequestHeader("X-Tenant-ID") UUID tenantId,
            @RequestParam UUID userId) throws Exception {
        TaxProfile profile = taxProfileService.getProfile(tenantId, userId).orElseThrow();
        byte[] pdfBytes = accountantIntegrationService.generateTaxPackagePdf(profile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "tax-package.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
