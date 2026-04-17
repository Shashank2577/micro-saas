package com.crosscutting.socialintelligence.service;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.crosscutting.socialintelligence.domain.PlatformAccount;
import com.crosscutting.socialintelligence.repository.PlatformAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class OAuthManager {

    private final PlatformAccountRepository accountRepository;

    public OAuthManager(PlatformAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String getAuthorizationUrl(String platform) {
        // Mock URL generation based on platform
        return "https://oauth.mock.com/authorize?client_id=MOCK_CLIENT&redirect_uri=MOCK_REDIRECT&state=" + platform;
    }

    @Transactional
    public PlatformAccount handleCallback(String platform, String code, String tenantId) {
        // Mock processing code and fetching token
        PlatformAccount account = PlatformAccount.builder()
                .tenantId(tenantId)
                .platformName(platform)
                .platformAccountId("mock_acc_id_" + UUID.randomUUID())
                .accountName(platform + " Account")
                .accessToken("mock_access_token_" + code)
                .refreshToken("mock_refresh_token_" + code)
                .tokenExpiresAt(ZonedDateTime.now().plusDays(60))
                .isActive(true)
                .build();
        return accountRepository.save(account);
    }

    @Transactional
    public void disconnectAccount(UUID accountId, String tenantId) {
        accountRepository.findByIdAndTenantId(accountId, tenantId).ifPresent(accountRepository::delete);
    }
}
