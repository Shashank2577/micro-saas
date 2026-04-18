package com.microsaas.integrationbridge;

import com.microsaas.integrationbridge.model.ApiCredential;
import com.microsaas.integrationbridge.repository.ApiCredentialRepository;
import com.microsaas.integrationbridge.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CredentialServiceTest {

    private ApiCredentialRepository repository;
    private CredentialService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ApiCredentialRepository.class);
        service = new CredentialService(repository);
        ReflectionTestUtils.setField(service, "encryptionKey", "1234567890123456");
    }

    @Test
    void testEncryptDecrypt() {
        UUID integrationId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        
        ApiCredential cred = new ApiCredential();
        cred.setEncryptedToken("some_encrypted_value");
        
        when(repository.findByIntegrationIdAndTenantId(integrationId, tenantId)).thenReturn(Optional.of(cred));
        when(repository.save(any(ApiCredential.class))).thenAnswer(i -> i.getArguments()[0]);
        
        ApiCredential saved = service.saveCredentials(integrationId, tenantId, "my_secret_token", null, null, null);
        assertNotEquals("my_secret_token", saved.getEncryptedToken());
        
        // Setup mock to return the newly saved cred for decryption
        when(repository.findByIntegrationIdAndTenantId(integrationId, tenantId)).thenReturn(Optional.of(saved));
        String decrypted = service.getDecryptedToken(integrationId, tenantId);
        
        assertEquals("my_secret_token", decrypted);
    }
}
