package com.microsaas.onboardflow.service;
import com.microsaas.onboardflow.model.Escalation;
import com.microsaas.onboardflow.repository.EscalationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class EscalationService {
    private final EscalationRepository repository;
    public EscalationService(EscalationRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<Escalation> findAll(UUID tenantId) { return repository.findByTenantId(tenantId); }
    @Transactional(readOnly = true)
    public Escalation findById(UUID id, UUID tenantId) { return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Not found")); }
    public Escalation create(Escalation entity, UUID tenantId) { entity.setId(UUID.randomUUID()); entity.setTenantId(tenantId); return repository.save(entity); }
    public Escalation update(UUID id, Escalation updateContent, UUID tenantId) {
        Escalation existing = findById(id, tenantId);
        if (updateContent.getName() != null) existing.setName(updateContent.getName());
        return repository.save(existing);
    }
}
