package com.crosscutting.socialintelligence.controller;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.crosscutting.socialintelligence.domain.PlatformAccount;
import com.crosscutting.socialintelligence.repository.PlatformAccountRepository;
import com.crosscutting.socialintelligence.service.OAuthManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private final PlatformAccountRepository accountRepository;
    private final OAuthManager oauthManager;

    public AccountsController(PlatformAccountRepository accountRepository, OAuthManager oauthManager) {
        this.accountRepository = accountRepository;
        this.oauthManager = oauthManager;
    }

    @GetMapping
    public ResponseEntity<List<PlatformAccount>> getAccounts(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(accountRepository.findByTenantId(tenantId));
    }

    @PostMapping("/connect/{platform}")
    public ResponseEntity<Map<String, String>> connect(@PathVariable String platform) {
        String url = oauthManager.getAuthorizationUrl(platform);
        return ResponseEntity.ok(Map.of("authorizationUrl", url));
    }

    @PostMapping("/callback")
    public ResponseEntity<PlatformAccount> callback(@RequestHeader("X-Tenant-ID") String tenantId,
                                                    @RequestParam String platform,
                                                    @RequestParam String code) {
        return ResponseEntity.ok(oauthManager.handleCallback(platform, code, tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disconnect(@RequestHeader("X-Tenant-ID") String tenantId, @PathVariable UUID id) {
        oauthManager.disconnectAccount(id, tenantId);
        return ResponseEntity.noContent().build();
    }
}
