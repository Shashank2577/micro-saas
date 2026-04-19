package com.microsaas.onboardflow.service;
import com.microsaas.onboardflow.model.CompletionSignal;
import com.microsaas.onboardflow.repository.CompletionSignalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class CompletionSignalService {
    private final CompletionSignalRepository repository;
    public CompletionSignalService(CompletionSignalRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<CompletionSignal> findAll(UUID tenantId) { return repository.findByTenantId(tenantId); }
    @Transactional(readOnly = true)
    public CompletionSignal findById(UUID id, UUID tenantId) { return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Not found")); }
    public CompletionSignal create(CompletionSignal entity, UUID tenantId) { entity.setId(UUID.randomUUID()); entity.setTenantId(tenantId); return repository.save(entity); }
    public CompletionSignal update(UUID id, CompletionSignal updateContent, UUID tenantId) {
        CompletionSignal existing = findById(id, tenantId);
        if (updateContent.getName() != null) existing.setName(updateContent.getName());
        return repository.save(existing);
    }
}
