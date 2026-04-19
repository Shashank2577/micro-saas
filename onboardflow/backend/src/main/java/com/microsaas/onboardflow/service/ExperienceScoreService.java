package com.microsaas.onboardflow.service;
import com.microsaas.onboardflow.model.ExperienceScore;
import com.microsaas.onboardflow.repository.ExperienceScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class ExperienceScoreService {
    private final ExperienceScoreRepository repository;
    public ExperienceScoreService(ExperienceScoreRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<ExperienceScore> findAll(UUID tenantId) { return repository.findByTenantId(tenantId); }
    @Transactional(readOnly = true)
    public ExperienceScore findById(UUID id, UUID tenantId) { return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Not found")); }
    public ExperienceScore create(ExperienceScore entity, UUID tenantId) { entity.setId(UUID.randomUUID()); entity.setTenantId(tenantId); return repository.save(entity); }
    public ExperienceScore update(UUID id, ExperienceScore updateContent, UUID tenantId) {
        ExperienceScore existing = findById(id, tenantId);
        if (updateContent.getName() != null) existing.setName(updateContent.getName());
        return repository.save(existing);
    }
}
