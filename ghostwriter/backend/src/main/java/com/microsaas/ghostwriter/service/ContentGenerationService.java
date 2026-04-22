package com.microsaas.ghostwriter.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.microsaas.ghostwriter.model.ContentRequest;
import com.microsaas.ghostwriter.model.Document;
import com.microsaas.ghostwriter.model.Revision;
import com.microsaas.ghostwriter.repository.ContentRequestRepository;
import com.microsaas.ghostwriter.repository.DocumentRepository;
import com.microsaas.ghostwriter.repository.RevisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentGenerationService {

    private final AiService aiService;
    private final ContentRequestRepository requestRepository;
    private final DocumentRepository documentRepository;
    private final RevisionRepository revisionRepository;

    @Transactional
    public ContentRequest generateContent(ContentRequest request, String tenantId) {
        request.setTenantId(tenantId);
        request.setStatus("PROCESSING");
        request = requestRepository.save(request);

        try {
            StringBuilder promptBuilder = new StringBuilder();
            if (request.getTemplate() != null) {
                promptBuilder.append("Structure/Template:\n").append(request.getTemplate().getStructure()).append("\n\n");
            }
            if (request.getPersona() != null) {
                promptBuilder.append("Target Audience/Persona:\n")
                             .append(request.getPersona().getDescription()).append("\n")
                             .append("Tone: ").append(request.getPersona().getTone()).append("\n\n");
            }
            if (request.getStyleGuide() != null) {
                promptBuilder.append("Style Guide:\n").append(request.getStyleGuide().getRules()).append("\n\n");
            }

            promptBuilder.append("Task:\n").append(request.getPrompt());

            String systemPrompt = "You are an expert ghostwriter. Generate content exactly as requested, adhering to all instructions.";

            ChatRequest aiRequest = new ChatRequest("gpt-4", List.of(
                new ChatMessage("system", systemPrompt),
                new ChatMessage("user", promptBuilder.toString())
            ), 0.7, 2000);

            String generatedContent = aiService.chat(aiRequest).content();

            request.setResult(generatedContent);
            request.setStatus("COMPLETED");

            if (request.getDocument() != null) {
                Document doc = request.getDocument();

                // Save current state as revision
                if (doc.getContent() != null && !doc.getContent().isEmpty()) {
                    Revision rev = new Revision();
                    rev.setDocument(doc);
                    rev.setContent(doc.getContent());
                    rev.setTenantId(tenantId);
                    revisionRepository.save(rev);
                }

                // Update document
                doc.setContent(generatedContent);
                documentRepository.save(doc);
            }

        } catch (Exception e) {
            log.error("AI Generation failed", e);
            request.setStatus("FAILED");
            request.setResult("Error: " + e.getMessage());
        }

        return requestRepository.save(request);
    }
}
