package com.microsaas.auditready.service;

import com.microsaas.auditready.domain.AuditFramework;
import com.microsaas.auditready.domain.Control;
import com.microsaas.auditready.repository.AuditFrameworkRepository;
import com.microsaas.auditready.repository.ControlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FrameworkService {
    
    private final AuditFrameworkRepository frameworkRepository;
    private final ControlRepository controlRepository;

    public List<AuditFramework> listFrameworks(UUID tenantId) {
        return frameworkRepository.findByTenantId(tenantId);
    }

    public Optional<AuditFramework> getFramework(UUID id, UUID tenantId) {
        return frameworkRepository.findByIdAndTenantId(id, tenantId);
    }

    public List<Control> getControls(UUID frameworkId, UUID tenantId) {
        return controlRepository.findByFrameworkIdAndTenantId(frameworkId, tenantId);
    }
}
