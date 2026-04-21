package com.microsaas.financenarrator.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AiOrchestrationServiceTest {

    @Mock
    private AiService aiService;

    @InjectMocks
    private AiOrchestrationService service;

    private Map<String, Object> requestData;

    @BeforeEach
    void setUp() {
        requestData = Map.of("metrics", "strong Q3 revenue");
    }

    @Test
    void testGenerateNarrative_callsLiteLLM() {
        ChatResponse.Usage usage = new ChatResponse.Usage(10, 10, 20);
        ChatResponse mockResponse = new ChatResponse("id123", "claude-sonnet-4-6", "Analyzed narrative output", usage);
        when(aiService.chat(any(ChatRequest.class))).thenReturn(mockResponse);

        String result = service.analyze(requestData);

        assertNotNull(result);
        assertEquals("Analyzed narrative output", result);
        verify(aiService, times(1)).chat(any(ChatRequest.class));
    }

    @Test
    void testGenerateNarrative_withMockedAiResponse() {
        ChatResponse.Usage usage = new ChatResponse.Usage(10, 10, 20);
        ChatResponse mockResponse = new ChatResponse("id456", "claude-sonnet-4-6", "Mocked recommendation", usage);
        when(aiService.chat(any(ChatRequest.class))).thenReturn(mockResponse);

        String result = service.recommend(requestData);

        assertNotNull(result);
        assertEquals("Mocked recommendation", result);
        verify(aiService, times(1)).chat(any(ChatRequest.class));
    }

    @Test
    void testGenerateNarrative_handlesError() {
        when(aiService.chat(any(ChatRequest.class))).thenThrow(new RuntimeException("LiteLLM timeout"));

        assertThrows(RuntimeException.class, () -> service.analyze(requestData));
        verify(aiService, times(1)).chat(any(ChatRequest.class));
    }
}
