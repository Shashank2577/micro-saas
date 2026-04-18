package com.microsaas.customersuccessos.service;

import com.microsaas.customersuccessos.model.*;
import com.microsaas.customersuccessos.repository.*;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class QbrGenerationServiceTest {

    @Mock
    private QbrDeckRepository qbrDeckRepository;

    @Mock
    private CustomerSuccessService customerSuccessService;
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private QbrGenerationService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        ReflectionTestUtils.setField(service, "aiGatewayUrl", "http://litellm:4000/v1/chat/completions");
        ReflectionTestUtils.setField(service, "aiApiKey", "dummy-key");
    }

    @Test
    void testGenerateQbr() {
        UUID accId = UUID.randomUUID();
        Account acc = new Account();
        acc.setName("Test");
        when(customerSuccessService.getAccount(accId)).thenReturn(acc);
        when(customerSuccessService.getLatestHealthScore(accId)).thenReturn(null);
        when(customerSuccessService.getExpansionOpportunities(accId)).thenReturn(Collections.emptyList());
        
        when(qbrDeckRepository.save(any(QbrDeck.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> choiceMap = new HashMap<>();
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("content", "Generated QBR");
        choiceMap.put("message", messageMap);
        responseMap.put("choices", Collections.singletonList(choiceMap));
        
        ResponseEntity<Map> responseEntity = ResponseEntity.ok(responseMap);
        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(responseEntity);
        
        QbrDeck qbr = service.generateQbr(accId);
        assertNotNull(qbr);
        assertEquals("COMPLETED", qbr.getStatus());
        assertEquals("Generated QBR", qbr.getContent());
    }
}
