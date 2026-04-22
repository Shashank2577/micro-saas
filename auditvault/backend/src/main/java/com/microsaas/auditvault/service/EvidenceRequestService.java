package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.model.EvidenceRequest;
import com.microsaas.auditvault.repository.EvidenceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceRequestService {
    private final EvidenceRequestRepository repository;

    public List<EvidenceRequest> listRequests() {
        return repository.findByTenantId(TenantContext.require());
    }
}
