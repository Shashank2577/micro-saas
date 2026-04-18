package com.microsaas.nonprofitos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.nonprofitos.ai.AiService;
import com.microsaas.nonprofitos.domain.Grant;
import com.microsaas.nonprofitos.dto.GrantDto;
import com.microsaas.nonprofitos.repository.GrantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrantServiceTest {

    @Mock
    private GrantRepository grantRepository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private GrantService grantService;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateGrant() {
        GrantDto dto = new GrantDto();
        dto.setTitle("Community Grant");
        dto.setFunder("Gates Foundation");

        Grant savedGrant = new Grant();
        savedGrant.setTitle("Community Grant");
        savedGrant.setStatus("DRAFT");

        when(grantRepository.save(any(Grant.class))).thenReturn(savedGrant);

        Grant result = grantService.createGrant(dto);

        assertEquals("Community Grant", result.getTitle());
        assertEquals("DRAFT", result.getStatus());
    }

    @Test
    void testGenerateDraft() {
        UUID grantId = UUID.randomUUID();
        Grant grant = new Grant();
        grant.setTitle("Education Grant");
        grant.setFunder("Ford Foundation");

        when(grantRepository.findByIdAndTenantId(grantId, tenantId)).thenReturn(Optional.of(grant));
        when(aiService.generateGrantDraft(grant.getTitle(), grant.getFunder())).thenReturn("Here is a draft...");
        when(grantRepository.save(any(Grant.class))).thenReturn(grant);

        String result = grantService.generateDraft(grantId);

        assertEquals("Here is a draft...", result);
        assertEquals("Here is a draft...", grant.getDraftContent());
    }
}
