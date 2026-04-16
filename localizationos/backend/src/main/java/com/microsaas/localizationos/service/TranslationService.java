package com.microsaas.localizationos.service;

import com.microsaas.localizationos.domain.*;
import com.microsaas.localizationos.dto.QualityMetrics;
import com.microsaas.localizationos.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationService {

    private final TranslationProjectRepository projectRepository;
    private final TranslationMemoryRepository memoryRepository;
    private final TranslationJobRepository jobRepository;
    private final CulturalFlagRepository culturalFlagRepository;

    @Transactional
    public TranslationProject createProject(String name, String sourceLanguage, List<String> targetLanguages, UUID tenantId) {
        TranslationProject project = TranslationProject.builder()
                .name(name)
                .sourceLanguage(sourceLanguage)
                .targetLanguages(targetLanguages)
                .status(ProjectStatus.ACTIVE)
                .tenantId(tenantId)
                .build();
        return projectRepository.save(project);
    }

    public List<TranslationProject> listProjects(UUID tenantId) {
        return projectRepository.findByTenantId(tenantId);
    }

    public TranslationProject getProject(UUID projectId, UUID tenantId) {
        return projectRepository.findByIdAndTenantId(projectId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    @Transactional
    public TranslationMemory addMemory(UUID projectId, String sourceLanguage, String targetLanguage, String sourceText, String translatedText, UUID tenantId) {
        TranslationProject project = getProject(projectId, tenantId);

        TranslationMemory memory = TranslationMemory.builder()
                .tenantId(tenantId)
                .projectId(project.getId())
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .sourceText(sourceText)
                .translatedText(translatedText)
                .approved(true)
                .usageCount(0)
                .build();

        return memoryRepository.save(memory);
    }

    @Transactional
    public TranslationJob submitTranslationJob(UUID projectId, String sourceContent, String sourceLanguage, String targetLanguage, UUID tenantId) {
        TranslationProject project = getProject(projectId, tenantId);

        TranslationJob job = TranslationJob.builder()
                .tenantId(tenantId)
                .projectId(project.getId())
                .sourceContent(sourceContent)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .status(JobStatus.PENDING)
                .build();
        
        job = jobRepository.save(job);

        // Here we would ideally integrate with an AI service via cc-starter
        // For demonstration, simulating AI processing:
        job.setTranslatedContent("[AI Translated] " + sourceContent);
        job.setAiConfidence(new BigDecimal("0.95"));
        job.setStatus(JobStatus.REVIEW_NEEDED);

        // Extract mock cultural flags
        CulturalFlag flag = CulturalFlag.builder()
                .tenantId(tenantId)
                .jobId(job.getId())
                .phrase("break a leg")
                .issueType(IssueType.IDIOM)
                .market("General " + targetLanguage)
                .suggestion("Use a local equivalent for 'good luck'")
                .severity(Severity.MEDIUM)
                .build();

        culturalFlagRepository.save(flag);
        
        List<Object> flagsList = new ArrayList<>();
        flagsList.add(flag);
        job.setCulturalFlags(flagsList);

        return jobRepository.save(job);
    }

    public TranslationJob getJob(UUID jobId, UUID tenantId) {
        return jobRepository.findByIdAndTenantId(jobId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }

    @Transactional
    public TranslationJob approveJob(UUID jobId, UUID tenantId) {
        TranslationJob job = getJob(jobId, tenantId);
        job.setStatus(JobStatus.APPROVED);
        
        // Add to translation memory
        addMemory(job.getProjectId(), job.getSourceLanguage(), job.getTargetLanguage(), job.getSourceContent(), job.getTranslatedContent(), tenantId);

        return jobRepository.save(job);
    }

    public List<CulturalFlag> getJobFlags(UUID jobId, UUID tenantId) {
        TranslationJob job = getJob(jobId, tenantId);
        return culturalFlagRepository.findByJobIdAndTenantId(job.getId(), tenantId);
    }

    public QualityMetrics getQualityMetrics(UUID projectId, UUID tenantId) {
        TranslationProject project = getProject(projectId, tenantId);
        List<TranslationJob> jobs = jobRepository.findByProjectIdAndTenantId(project.getId(), tenantId);

        int totalJobs = jobs.size();
        int approvedJobs = 0;
        int pendingReviewJobs = 0;
        double totalConfidence = 0.0;
        int jobsWithConfidence = 0;
        int totalFlags = 0;

        for (TranslationJob job : jobs) {
            if (job.getStatus() == JobStatus.APPROVED) {
                approvedJobs++;
            } else if (job.getStatus() == JobStatus.REVIEW_NEEDED) {
                pendingReviewJobs++;
            }
            if (job.getAiConfidence() != null) {
                totalConfidence += job.getAiConfidence().doubleValue();
                jobsWithConfidence++;
            }
            if (job.getCulturalFlags() != null) {
                totalFlags += job.getCulturalFlags().size();
            }
        }

        double averageConfidence = jobsWithConfidence > 0 ? totalConfidence / jobsWithConfidence : 0.0;

        return QualityMetrics.builder()
                .totalJobs(totalJobs)
                .approvedJobs(approvedJobs)
                .pendingReviewJobs(pendingReviewJobs)
                .averageConfidence(averageConfidence)
                .totalCulturalFlags(totalFlags)
                .build();
    }
}
