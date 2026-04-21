package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.model.ComplianceFinding;
import com.microsaas.auditvault.repository.ComplianceFindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplianceFindingService {
    private final ComplianceFindingRepository repository;

    public List<ComplianceFinding> listFindings() {
        return repository.findByTenantId(TenantContext.require());
    }
}
