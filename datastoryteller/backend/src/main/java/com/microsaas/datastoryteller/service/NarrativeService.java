package com.microsaas.datastoryteller.service;

import com.microsaas.datastoryteller.domain.model.*;
import com.microsaas.datastoryteller.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NarrativeService {

    private final NarrativeReportRepository reportRepository;
    private final NarrativeTemplateRepository templateRepository;
    private final FeedbackRepository feedbackRepository;
    private final InsightRepository insightRepository;
    private final DatasetRepository datasetRepository;
    private final ScheduledDeliveryRepository deliveryRepository;
    private final AiService aiService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public NarrativeReport generateNarrative(String tenantId, UUID datasetId, UUID templateId, OffsetDateTime start, OffsetDateTime end) {
        Dataset dataset = datasetRepository.findByIdAndTenantId(datasetId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));
        NarrativeTemplate template = templateRepository.findByIdAndTenantId(templateId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        String systemPrompt = "You are an expert data analyst. Generate a markdown narrative referencing at least 3 metrics based on the provided data context.";
        String userPrompt = "Generate narrative using template: " + template.getPromptTemplate() + "\nData Schema: " + dataset.getSchemaJson();

        String markdown = aiService.generateNarrative("generate-narrative", tenantId, systemPrompt, userPrompt);

        NarrativeReport report = NarrativeReport.builder()
                .tenantId(tenantId)
                .dataset(dataset)
                .template(template)
                .title("Generated Narrative - " + OffsetDateTime.now())
                .contentMarkdown(markdown)
                .timeRangeStart(start)
                .timeRangeEnd(end)
                .status(ReportStatus.DRAFT)
                .generatedAt(OffsetDateTime.now())
                .model("claude-sonnet-4-6")
                .build();

        reportRepository.save(report);

        // Emit generated event (could use cc-starter event publisher)
        // Here we just save the entity, event will be handled by webhook or spring event
        return report;
    }

    public Page<NarrativeReport> listNarratives(String tenantId, UUID datasetId, String statusStr, Pageable pageable) {
        ReportStatus status = statusStr != null ? ReportStatus.valueOf(statusStr) : null;
        return reportRepository.findByTenantIdAndFilters(tenantId, datasetId, status, pageable);
    }

    public NarrativeReport getNarrative(UUID id, String tenantId) {
        return reportRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Narrative not found"));
    }

    @Transactional
    public NarrativeReport publishNarrative(UUID id, String tenantId) {
        NarrativeReport report = getNarrative(id, tenantId);
        report.setStatus(ReportStatus.PUBLISHED);
        report = reportRepository.save(report);
        // Event emitting can be done by standard spring event
        eventPublisher.publishEvent(new NarrativePublishedEvent(this, report));
        return report;
    }

    @Transactional
    public Feedback addFeedback(UUID reportId, String tenantId, int rating, String comment, String userId) {
        NarrativeReport report = getNarrative(reportId, tenantId);
        Feedback feedback = Feedback.builder()
                .tenantId(tenantId)
                .report(report)
                .rating(rating)
                .comment(comment)
                .userId(userId)
                .build();
        return feedbackRepository.save(feedback);
    }

    public String askQuestion(String tenantId, UUID datasetId, String question) {
        Dataset dataset = datasetRepository.findByIdAndTenantId(datasetId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));

        String systemPrompt = "You are an AI analyst. Write a SQL query, execute mentally, and provide a prose answer.";
        String userPrompt = "Schema: " + dataset.getSchemaJson() + "\nQuestion: " + question;

        return aiService.generateNarrative("narrative-qa", tenantId, systemPrompt, userPrompt);
    }

    public String describeChart(String tenantId, String imageBase64) {
        String systemPrompt = "You are a visual data analyst. Describe the provided chart in at least 50 words grounded in what the chart shows.";
        String userPrompt = "Describe this chart.";
        return aiService.explainChart("chart-describe", tenantId, systemPrompt, imageBase64, userPrompt);
    }

    public List<NarrativeTemplate> listTemplates(String tenantId) {
        return templateRepository.findByTenantId(tenantId);
    }

    @Transactional
    public NarrativeTemplate createTemplate(NarrativeTemplate template) {
        return templateRepository.save(template);
    }

    @Transactional
    public ScheduledDelivery createSchedule(ScheduledDelivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public List<ScheduledDelivery> listSchedules(String tenantId) {
        return deliveryRepository.findByTenantId(tenantId);
    }

    @Transactional
    public void deleteSchedule(UUID id, String tenantId) {
        deliveryRepository.deleteByIdAndTenantId(id, tenantId);
    }

    public Insight attributeDelta(String tenantId, UUID datasetId, String metric, double deltaPercent) {
        // Mock attribution for the AC
        Dataset dataset = datasetRepository.findByIdAndTenantId(datasetId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found"));

        String prompt = "Decompose a " + deltaPercent + "% delta in " + metric + " into at least 3 segment drivers with contribution %.";
        String analysis = aiService.generateNarrative("attribute-metric", tenantId, "You are a root cause analyst.", prompt);

        Insight insight = Insight.builder()
                .tenantId(tenantId)
                .type(InsightType.SEGMENT_DRIVER)
                .headline(metric + " attribution analysis")
                .description(analysis)
                .severity(InsightSeverity.INFO)
                .build();
        return insight;
    }
}
