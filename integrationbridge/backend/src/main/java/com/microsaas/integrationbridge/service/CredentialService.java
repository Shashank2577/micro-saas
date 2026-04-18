package com.microsaas.integrationbridge.service;

import com.microsaas.integrationbridge.model.ApiCredential;
import com.microsaas.integrationbridge.repository.ApiCredentialRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class CredentialService {

    private final ApiCredentialRepository apiCredentialRepository;
    
    @Value("${integrationbridge.encryption.key:1234567890123456}") // Default 16-byte key for dev
    private String encryptionKey;

    public CredentialService(ApiCredentialRepository apiCredentialRepository) {
        this.apiCredentialRepository = apiCredentialRepository;
    }

    private String encrypt(String plainText) {
        if (plainText == null) return null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting: " + e.toString());
        }
    }

    private String decrypt(String cipherText) {
        if (cipherText == null) return null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting: " + e.toString());
        }
    }

    @Transactional
    public ApiCredential saveCredentials(UUID integrationId, UUID tenantId, String token, String refreshToken, String username, String password) {
        Optional<ApiCredential> existing = apiCredentialRepository.findByIntegrationIdAndTenantId(integrationId, tenantId);
        ApiCredential cred = existing.orElse(new ApiCredential());
        cred.setIntegrationId(integrationId);
        cred.setTenantId(tenantId);
        if (token != null) cred.setEncryptedToken(encrypt(token));
        if (refreshToken != null) cred.setRefreshToken(encrypt(refreshToken));
        if (username != null) cred.setUsername(username);
        if (password != null) cred.setEncryptedPassword(encrypt(password));
        
        return apiCredentialRepository.save(cred);
    }
    
    public String getDecryptedToken(UUID integrationId, UUID tenantId) {
        Optional<ApiCredential> cred = apiCredentialRepository.findByIntegrationIdAndTenantId(integrationId, tenantId);
        return cred.map(c -> decrypt(c.getEncryptedToken())).orElse(null);
    }
}
