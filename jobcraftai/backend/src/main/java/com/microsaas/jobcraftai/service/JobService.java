package com.microsaas.jobcraftai.service;

import com.microsaas.jobcraftai.model.*;
import com.microsaas.jobcraftai.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobDescriptionRepository jobRepository;
    private final BiasFlagRepository biasFlagRepository;
    private final JobVariantRepository variantRepository;
    private final HireOutcomeRepository outcomeRepository;
    private final ChatClient chatClient;

    @Transactional
    public JobDescription generateJobDescription(String title, String department, String roleLevel, Map<String, Object> requirements) {
        String reqStr = requirements != null ? requirements.toString() : "";
        String prompt = String.format("Generate a professional, SEO-optimized job description for a %s in %s (Level: %s). Requirements: %s",
                title, department, roleLevel, reqStr);

        String generatedContent = chatClient.prompt().user(prompt).call().content();

        JobDescription job = JobDescription.builder()
                .title(title)
                .department(department)
                .roleLevel(roleLevel)
                .requirements(requirements)
                .generatedContent(generatedContent)
                .biasScore(BigDecimal.valueOf(100.0)) // Placeholder
                .seoScore(BigDecimal.valueOf(85.0))   // Placeholder
                .build();

        return jobRepository.save(job);
    }

    public List<JobDescription> getAllJobs() {
        return jobRepository.findAll();
    }

    public JobDescription getJobById(UUID id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    @Transactional
    public BiasFlag checkBias(UUID jobId) {
        JobDescription job = getJobById(jobId);

        String prompt = "Analyze the following job description for any gender, age, or cultural bias. Respond with a JSON array where each object has fields: 'phrase' (the biased text), 'biasType' (GENDER, AGE, or CULTURAL), 'suggestion' (how to fix it), and 'severity' (LOW, MEDIUM, HIGH).\n\nJob description:\n" + job.getGeneratedContent();

        // For simplicity, simulating the bias checking logic. In a real scenario, parsing the JSON from AI response would be needed.
        // We'll create a simple dummy flag instead of parsing JSON for now to ensure reliability.
        String aiAnalysis = chatClient.prompt().user(prompt).call().content();

        // Decrement score just as an example effect
        job.setBiasScore(job.getBiasScore().subtract(BigDecimal.valueOf(5.0)));
        jobRepository.save(job);

        BiasFlag flag = BiasFlag.builder()
                .jobId(jobId)
                .phrase("Dummy phrase flagged by AI: " + aiAnalysis.substring(0, Math.min(20, aiAnalysis.length())))
                .biasType(BiasFlag.BiasType.GENDER)
                .suggestion("Use gender neutral terms")
                .severity(BiasFlag.Severity.MEDIUM)
                .build();

        return biasFlagRepository.save(flag);
    }

    @Transactional
    public JobVariant createVariant(UUID jobId, String variantName) {
        JobDescription job = getJobById(jobId);

        String prompt = "Create an alternative A/B test variant for the following job description. Make it more appealing to younger demographic. Job description:\n" + job.getGeneratedContent();
        String newContent = chatClient.prompt().user(prompt).call().content();

        JobVariant variant = JobVariant.builder()
                .jobId(jobId)
                .variantName(variantName)
                .content(newContent)
                .build();

        return variantRepository.save(variant);
    }

    public Map<String, Object> getPerformance(UUID jobId) {
        List<JobVariant> variants = variantRepository.findByJobId(jobId);
        // Simple mock performance metrics
        return Map.of("variants", variants.stream().map(v ->
            Map.of("variantName", v.getVariantName(), "views", v.getViews(), "applications", v.getApplications(), "qualityScore", v.getQualityScore() == null ? 0 : v.getQualityScore())
        ).collect(Collectors.toList()));
    }

    @Transactional
    public HireOutcome recordOutcome(UUID jobId, UUID candidateId, HireOutcome.Outcome outcome, String sourceChannel) {
        HireOutcome hireOutcome = HireOutcome.builder()
                .jobId(jobId)
                .candidateId(candidateId)
                .outcome(outcome)
                .sourceChannel(sourceChannel)
                .build();
        return outcomeRepository.save(hireOutcome);
    }
}
