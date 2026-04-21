package com.crosscutting.socialintelligence.service;

import com.crosscutting.socialintelligence.domain.PlatformAccount;
import com.crosscutting.socialintelligence.repository.PlatformAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final PlatformAccountRepository repository;

    public PlatformAccount connectAccount(UUID tenantId, String platform, String accountIdExternal) {
        PlatformAccount acc = PlatformAccount.builder()
                .tenantId(tenantId)
                .platform(platform)
                .accountIdExternal(accountIdExternal)
                .accountName("User " + accountIdExternal)
                .accessTokenEncrypted("encrypted_access")
                .refreshTokenEncrypted("encrypted_refresh")
                .isActive(true)
                .build();
        return repository.save(acc);
    }

    public List<PlatformAccount> getAccounts(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public void disconnectAccount(UUID tenantId, UUID accountId) {
        repository.deleteById(accountId);
    }
}
