package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.CustomerSegment;
import com.microsaas.pricingintelligence.repository.CustomerSegmentRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SegmentationService {

    private final CustomerSegmentRepository customerSegmentRepository;

    public List<CustomerSegment> getSegments() {
        UUID tenantId = TenantContext.require();
        return customerSegmentRepository.findByTenantId(tenantId);
    }
}
