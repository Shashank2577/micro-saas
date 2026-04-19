package com.microsaas.complianceradar.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.complianceradar.domain.JurisdictionRule;
import com.microsaas.complianceradar.repository.JurisdictionRuleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NormalizationServiceTest {

    @Mock
    private JurisdictionRuleRepository repository;

    @Mock
    private AiService aiService;

    @InjectMocks
    private NormalizationService service;

    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testList() {
        when(repository.findAllByTenantId(tenantId)).thenReturn(List.of(new JurisdictionRule()));
        List<JurisdictionRule> result = service.list();
        assertEquals(1, result.size());
    }

    @Test
    void testCreate() {
        JurisdictionRule rule = JurisdictionRule.builder().name("Rule").build();
        when(repository.save(any(JurisdictionRule.class))).thenAnswer(i -> i.getArguments()[0]);
        JurisdictionRule result = service.create(rule);
        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
    }

    @Test
    void testAnalyzeText() {
        when(aiService.chat(any(ChatRequest.class))).thenReturn(new ChatResponse("id", "model", "analysis result", new ChatResponse.Usage(0,0,0)));
        String result = service.analyzeText("some text");
        assertEquals("analysis result", result);
    }
}
