package com.microsaas.ghostwriter.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatResponse;
import com.microsaas.ghostwriter.model.ContentRequest;
import com.microsaas.ghostwriter.model.Template;
import com.microsaas.ghostwriter.repository.ContentRequestRepository;
import com.microsaas.ghostwriter.repository.DocumentRepository;
import com.microsaas.ghostwriter.repository.RevisionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContentGenerationServiceTest {

    @Mock
    private AiService aiService;
    @Mock
    private ContentRequestRepository requestRepository;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private RevisionRepository revisionRepository;

    @InjectMocks
    private ContentGenerationService service;

    @Test
    void testGenerateContent_Success() {
        ContentRequest request = new ContentRequest();
        request.setPrompt("Write a blog post");
        Template template = new Template();
        template.setStructure("Intro, Body, Conclusion");
        request.setTemplate(template);

        when(requestRepository.save(any(ContentRequest.class))).thenReturn(request);
        ChatResponse mockResponse = new ChatResponse("id", "gpt-4", "Generated content", new ChatResponse.Usage(10, 20, 30));
        when(aiService.chat(any(ChatRequest.class))).thenReturn(mockResponse);

        ContentRequest result = service.generateContent(request, "tenant-1");

        assertEquals("COMPLETED", result.getStatus());
        assertEquals("Generated content", result.getResult());
        verify(aiService).chat(any(ChatRequest.class));
        verify(requestRepository, times(2)).save(any(ContentRequest.class));
    }
}
