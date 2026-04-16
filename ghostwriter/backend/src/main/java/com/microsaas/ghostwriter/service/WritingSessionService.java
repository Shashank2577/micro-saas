package com.microsaas.ghostwriter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.ghostwriter.model.*;
import com.microsaas.ghostwriter.repository.DraftSectionRepository;
import com.microsaas.ghostwriter.repository.ResearchNoteRepository;
import com.microsaas.ghostwriter.repository.VoiceModelRepository;
import com.microsaas.ghostwriter.repository.WritingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WritingSessionService {

    private final WritingSessionRepository sessionRepository;
    private final DraftSectionRepository draftSectionRepository;
    private final ResearchNoteRepository researchNoteRepository;
    private final VoiceModelRepository voiceModelRepository;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<WritingSession> listSessions(UUID tenantId) {
        return sessionRepository.findByTenantId(tenantId);
    }

    @Transactional
    public WritingSession startSession(String title, String topic, Integer targetWordCount, UUID voiceModelId, UUID tenantId) {
        WritingSession session = WritingSession.builder()
                .title(title)
                .topic(topic)
                .targetWordCount(targetWordCount != null ? targetWordCount : 1000)
                .voiceModelId(voiceModelId)
                .tenantId(tenantId)
                .status(SessionStatus.OUTLINE)
                .build();

        return sessionRepository.save(session);
    }

    @Transactional
    public DraftSection generateOutline(UUID sessionId, UUID tenantId) {
        WritingSession session = sessionRepository.findByIdAndTenantId(sessionId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        String voiceStyle = getVoiceStyle(session.getVoiceModelId(), tenantId);
        String outlineContent = aiService.generateOutline(session.getTopic(), voiceStyle);

        DraftSection outline = DraftSection.builder()
                .sessionId(sessionId)
                .tenantId(tenantId)
                .heading("Outline")
                .content(outlineContent)
                .sectionOrder(0)
                .build();

        session.setStatus(SessionStatus.DRAFTING);
        sessionRepository.save(session);

        return draftSectionRepository.save(outline);
    }

    @Transactional
    public DraftSection draftSection(UUID sessionId, String heading, Integer sectionOrder, UUID tenantId) {
        WritingSession session = sessionRepository.findByIdAndTenantId(sessionId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        // Get outline context
        List<DraftSection> sections = draftSectionRepository.findBySessionIdAndTenantIdOrderBySectionOrderAsc(sessionId, tenantId);
        String outlineContext = sections.stream()
                .filter(s -> s.getSectionOrder() == 0)
                .findFirst()
                .map(DraftSection::getContent)
                .orElse("No outline available");

        String voiceStyle = getVoiceStyle(session.getVoiceModelId(), tenantId);
        String content = aiService.draftSection(session.getTopic(), heading, outlineContext, voiceStyle);

        DraftSection section = DraftSection.builder()
                .sessionId(sessionId)
                .tenantId(tenantId)
                .heading(heading)
                .content(content)
                .sectionOrder(sectionOrder != null ? sectionOrder : sections.size() + 1)
                .build();

        // Update current draft on session
        StringBuilder fullDraft = new StringBuilder(session.getCurrentDraft() != null ? session.getCurrentDraft() : "");
        fullDraft.append("\n\n## ").append(heading).append("\n").append(content);
        session.setCurrentDraft(fullDraft.toString());
        sessionRepository.save(session);

        return draftSectionRepository.save(section);
    }

    @Transactional
    public List<ResearchNote> researchTopic(UUID sessionId, UUID tenantId) {
        WritingSession session = sessionRepository.findByIdAndTenantId(sessionId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        String researchJson = aiService.researchTopic(session.getTopic());

        try {
            // Clean up JSON block formatting if present
            researchJson = researchJson.replaceAll("```json", "").replaceAll("```", "").trim();

            List<Map<String, String>> parsedNotes = objectMapper.readValue(researchJson, new TypeReference<>() {});

            List<ResearchNote> notes = parsedNotes.stream().map(note -> ResearchNote.builder()
                    .sessionId(sessionId)
                    .tenantId(tenantId)
                    .excerpt(note.getOrDefault("excerpt", "No excerpt provided"))
                    .citation(note.getOrDefault("citation", "Unknown source"))
                    .sourceUrl(note.getOrDefault("sourceUrl", ""))
                    .build()).toList();

            return researchNoteRepository.saveAll(notes);
        } catch (Exception e) {
            // Fallback if parsing fails
            ResearchNote fallbackNote = ResearchNote.builder()
                    .sessionId(sessionId)
                    .tenantId(tenantId)
                    .excerpt(researchJson)
                    .citation("AI Generated Research")
                    .build();
            return List.of(researchNoteRepository.save(fallbackNote));
        }
    }

    private String getVoiceStyle(UUID voiceModelId, UUID tenantId) {
        if (voiceModelId == null) {
            return "Professional, clear, and engaging";
        }

        return voiceModelRepository.findByIdAndTenantId(voiceModelId, tenantId)
                .map(VoiceModel::getStyleAttributes)
                .map(attrs -> (String) attrs.get("extracted_style"))
                .orElse("Professional, clear, and engaging");
    }
}
