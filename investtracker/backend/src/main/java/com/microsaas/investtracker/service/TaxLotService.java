package com.microsaas.investtracker.service;

import com.microsaas.investtracker.dto.TaxLotDto;
import com.microsaas.investtracker.repository.TaxLotRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxLotService {
    private final TaxLotRepository taxLotRepository;

    public List<TaxLotDto> getTaxLots(UUID holdingId) {
        // Mock implementation
        UUID tenantId = TenantContext.require();
        return Collections.emptyList();
    }
}