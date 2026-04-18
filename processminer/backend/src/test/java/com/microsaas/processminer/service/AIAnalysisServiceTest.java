package com.microsaas.processminer.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;

class AIAnalysisServiceTest {

    @Test
    void analyzeOpportunity() throws Exception {
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        
        String mockResponseJson = "{\"roi\": 25.5, \"effort\": \"LOW\", \"rationale\": \"Simple review task\"}";
        
        Map<String, Object> mockBody = Map.of(
            "choices", List.of(
                Map.of("message", Map.of("content", mockResponseJson))
            )
        );
        
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockBody, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
               .thenReturn(responseEntity);

        AIAnalysisService service = new AIAnalysisService(restTemplate, "http://mock", "key", "model", objectMapper);
        
        AIAnalysisService.OpportunityScore score = service.analyzeOpportunity("Manual Review", "Process A");
        
        assertEquals(new BigDecimal("25.5"), score.roi());
        assertEquals("LOW", score.effort());
        assertEquals("Simple review task", score.rationale());
    }

    @Test
    void analyzeOpportunity_fallback() {
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        ObjectMapper objectMapper = new ObjectMapper();
        
        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Map.class)))
               .thenThrow(new RuntimeException("Connection refused"));

        AIAnalysisService service = new AIAnalysisService(restTemplate, "http://mock", "key", "model", objectMapper);
        
        AIAnalysisService.OpportunityScore score = service.analyzeOpportunity("Manual Review", "Process A");
        
        assertEquals(new BigDecimal("15.5"), score.roi());
        assertEquals("MEDIUM", score.effort());
        assertTrue(score.rationale().contains("Fallback"));
    }
}
