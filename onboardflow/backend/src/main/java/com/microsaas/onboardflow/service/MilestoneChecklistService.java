package com.microsaas.onboardflow.service;
import com.microsaas.onboardflow.model.MilestoneChecklist;
import com.microsaas.onboardflow.repository.MilestoneChecklistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class MilestoneChecklistService {
    private final MilestoneChecklistRepository repository;
    public MilestoneChecklistService(MilestoneChecklistRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<MilestoneChecklist> findAll(UUID tenantId) { return repository.findByTenantId(tenantId); }
    @Transactional(readOnly = true)
    public MilestoneChecklist findById(UUID id, UUID tenantId) { return repository.findByIdAndTenantId(id, tenantId).orElseThrow(() -> new RuntimeException("Not found")); }
    public MilestoneChecklist create(MilestoneChecklist entity, UUID tenantId) { entity.setId(UUID.randomUUID()); entity.setTenantId(tenantId); return repository.save(entity); }
    public MilestoneChecklist update(UUID id, MilestoneChecklist updateContent, UUID tenantId) {
        MilestoneChecklist existing = findById(id, tenantId);
        if (updateContent.getName() != null) existing.setName(updateContent.getName());
        return repository.save(existing);
    }
}
