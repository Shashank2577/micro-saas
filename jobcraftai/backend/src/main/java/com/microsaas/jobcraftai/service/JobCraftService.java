package com.microsaas.jobcraftai.service;

import com.microsaas.jobcraftai.model.JobPosting;
import com.microsaas.jobcraftai.repository.JobPostingRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.webhooks.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class JobCraftService {

    private final JobPostingRepository repository;
    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    public JobCraftService(JobPostingRepository repository, WebhookService webhookService, ObjectMapper objectMapper) {
        this.repository = repository;
        this.webhookService = webhookService;
        this.objectMapper = objectMapper;
    }

    public List<JobPosting> getAllForTenant() {
        return repository.findByTenantId(TenantContext.require());
    }

    public Optional<JobPosting> getById(UUID id) {
        return repository.findById(id)
                .filter(p -> p.getTenantId().equals(TenantContext.require()));
    }

    @Transactional
    public JobPosting create(JobPosting posting) {
        posting.setId(UUID.randomUUID());
        posting.setTenantId(TenantContext.require());
        posting.setStatus("DRAFT");
        posting.setCreatedAt(LocalDateTime.now());
        posting.setUpdatedAt(LocalDateTime.now());
        return repository.save(posting);
    }

    @Transactional
    public JobPosting optimize(UUID id) {
        JobPosting posting = getById(id).orElseThrow(() -> new RuntimeException("Job posting not found"));

        // Stub AI optimization logic
        String originalDesc = posting.getDescription();
        String optimized = "[AI OPTIMIZED] " + originalDesc + "\n\nKeywords: AI, Cloud, Java, Microservices";

        posting.setOptimizedDescription(optimized);
        posting.setStatus("OPTIMIZED");
        posting.setUpdatedAt(LocalDateTime.now());

        return repository.save(posting);
    }

    @Transactional
    public JobPosting publish(UUID id) {
        JobPosting posting = getById(id).orElseThrow(() -> new RuntimeException("Job posting not found"));

        posting.setStatus("PUBLISHED");
        posting.setUpdatedAt(LocalDateTime.now());
        JobPosting saved = repository.save(posting);

        emitPublishedEvent(saved);

        return saved;
    }

    private void emitPublishedEvent(JobPosting posting) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                "event_id", UUID.randomUUID().toString(),
                "tenant_id", posting.getTenantId().toString(),
                "job_id", posting.getId().toString(),
                "title", posting.getTitle(),
                "timestamp", LocalDateTime.now().toString()
            ));
            webhookService.dispatch(posting.getTenantId(), "jobcraftai.posting.published", payload);
        } catch (Exception e) {
            // In a real app, we might want to retry or handle this better
            System.err.println("Failed to emit published event: " + e.getMessage());
        }
    }
}
