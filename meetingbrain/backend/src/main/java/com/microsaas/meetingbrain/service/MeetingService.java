package com.microsaas.meetingbrain.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.meetingbrain.dto.AnalysisResult;
import com.microsaas.meetingbrain.model.*;
import com.microsaas.meetingbrain.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private static final Logger log = LoggerFactory.getLogger(MeetingService.class);

    private final MeetingRepository meetingRepository;
    private final TranscriptLineRepository transcriptLineRepository;
    private final DecisionRepository decisionRepository;
    private final ActionItemRepository actionItemRepository;
    private final OpenQuestionRepository openQuestionRepository;
    private final ProjectRepository projectRepository;
    private final AiService aiService;
    private final ApplicationEventPublisher eventPublisher;

    public MeetingService(MeetingRepository meetingRepository,
                          TranscriptLineRepository transcriptLineRepository,
                          DecisionRepository decisionRepository,
                          ActionItemRepository actionItemRepository,
                          OpenQuestionRepository openQuestionRepository,
                          ProjectRepository projectRepository,
                          AiService aiService,
                          ApplicationEventPublisher eventPublisher) {
        this.meetingRepository = meetingRepository;
        this.transcriptLineRepository = transcriptLineRepository;
        this.decisionRepository = decisionRepository;
        this.actionItemRepository = actionItemRepository;
        this.openQuestionRepository = openQuestionRepository;
        this.projectRepository = projectRepository;
        this.aiService = aiService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public List<Meeting> getAllMeetings() {
        UUID tenantId = TenantContext.require();
        return meetingRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public Meeting getMeeting(UUID id) {
        return meetingRepository.findById(id).orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    @Transactional
    public Meeting createMeeting(Meeting meeting) {
        meeting.setTenantId(TenantContext.require());
        return meetingRepository.save(meeting);
    }

    @Transactional(readOnly = true)
    public List<TranscriptLine> getTranscript(UUID meetingId) {
        return transcriptLineRepository.findByTenantIdAndMeetingIdOrderByStartTimestampAsc(TenantContext.require(), meetingId);
    }

    @Transactional
    public void analyzeMeeting(UUID meetingId) {
        UUID tenantId = TenantContext.require();
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));

        if (!meeting.getTenantId().equals(tenantId)) {
            throw new RuntimeException("Unauthorized");
        }

        List<TranscriptLine> lines = transcriptLineRepository.findByTenantIdAndMeetingIdOrderByStartTimestampAsc(tenantId, meetingId);
        String transcriptText = lines.stream()
                .map(l -> l.getSpeaker() + ": " + l.getText())
                .collect(Collectors.joining("\n"));

        AnalysisResult result = aiService.analyzeTranscript(transcriptText);

        if (result != null) {
            meeting.setSummary(result.getSummary());
            meeting.setStatus("ANALYZED");
            meetingRepository.save(meeting);

            if (result.getDecisions() != null) {
                for (AnalysisResult.DecisionData d : result.getDecisions()) {
                    float[] embedding = aiService.generateEmbedding(d.getDecisionText());
                    Decision decision = new Decision();
                    decision.setTenantId(tenantId);
                    decision.setMeetingId(meetingId);
                    decision.setTopic(d.getTopic());
                    decision.setDescription(d.getDescription());
                    decision.setDecisionText(d.getDecisionText());
                    decision.setEmbedding(embedding);
                    decisionRepository.save(decision);
                }
            }

            if (result.getActionItems() != null) {
                for (AnalysisResult.ActionItemData a : result.getActionItems()) {
                    OffsetDateTime dueDate = null;
                    if (a.getDueDate() != null && !a.getDueDate().isEmpty()) {
                        try {
                            dueDate = LocalDate.parse(a.getDueDate()).atStartOfDay().atOffset(ZoneOffset.UTC);
                        } catch (DateTimeParseException e) {
                            log.warn("Invalid date format: {}", a.getDueDate());
                        }
                    }
                    ActionItem item = new ActionItem();
                    item.setTenantId(tenantId);
                    item.setMeetingId(meetingId);
                    item.setDescription(a.getDescription());
                    item.setOwner(a.getOwner());
                    item.setDueDate(dueDate);
                    item.setStatus("OPEN");
                    actionItemRepository.save(item);
                }
            }

            if (result.getOpenQuestions() != null) {
                for (AnalysisResult.QuestionData q : result.getOpenQuestions()) {
                    OpenQuestion question = new OpenQuestion();
                    question.setTenantId(tenantId);
                    question.setMeetingId(meetingId);
                    question.setQuestionText(q.getQuestionText());
                    question.setStatus("OPEN");
                    openQuestionRepository.save(question);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Decision> getAllDecisions() {
        return decisionRepository.findByTenantId(TenantContext.require());
    }

    @Transactional(readOnly = true)
    public List<Decision> searchDecisions(String query) {
        UUID tenantId = TenantContext.require();
        float[] embeddingArray = aiService.generateEmbedding(query);
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embeddingArray.length; i++) {
            sb.append(embeddingArray[i]);
            if (i < embeddingArray.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        
        return decisionRepository.searchByEmbedding(tenantId, sb.toString(), 10);
    }

    @Transactional(readOnly = true)
    public List<ActionItem> getAllActionItems() {
        return actionItemRepository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public ActionItem updateActionItemStatus(UUID id, String status) {
        ActionItem item = actionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ActionItem not found"));
        if (!item.getTenantId().equals(TenantContext.require())) {
            throw new RuntimeException("Unauthorized");
        }
        item.setStatus(status);
        return actionItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findByTenantId(TenantContext.require());
    }
}
