package com.microsaas.apimanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.apimanager.model.ApiKey;
import com.microsaas.apimanager.repository.ApiKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.security.MessageDigest;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public List<ApiKey> getKeys(UUID projectId) {
        return apiKeyRepository.findByProjectIdAndTenantId(projectId, TenantContext.require().toString());
    }

    public String generateKey(ApiKey keyRequest) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String rawKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String prefix = rawKey.substring(0, 8);
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawKey.getBytes());
            String keyHash = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

            keyRequest.setId(UUID.randomUUID());
            keyRequest.setTenantId(TenantContext.require().toString());
            keyRequest.setKeyHash(keyHash);
            keyRequest.setPrefix(prefix);
            keyRequest.setStatus("ACTIVE");
            keyRequest.setCreatedAt(LocalDateTime.now());
            apiKeyRepository.save(keyRequest); System.out.println("Emitting api.key.generated");

            return rawKey;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not hash key", e);
        }
    }

    public boolean revokeKey(UUID keyId) {
        Optional<ApiKey> key = apiKeyRepository.findByIdAndTenantId(keyId, TenantContext.require().toString());
        if (key.isPresent()) {
            ApiKey k = key.get();
            k.setStatus("REVOKED");
            apiKeyRepository.save(k); System.out.println("Emitting api.key.revoked");
            return true;
        }
        return false;
    }
}
