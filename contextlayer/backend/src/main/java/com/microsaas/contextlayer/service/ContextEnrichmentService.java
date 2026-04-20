package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.dto.ContextUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContextEnrichmentService {
    private final ContextRetrievalService contextRetrievalService;
    private final ContextUpdateService contextUpdateService;

    public void enrichContext(String customerId) {
        Optional<CustomerContext> contextOpt = contextRetrievalService.getContext(customerId);
        if (contextOpt.isPresent()) {
            CustomerContext context = contextOpt.get();
            // Simplified enrichment logic for LTV or Segment based on current attributes
            ContextUpdateDTO update = new ContextUpdateDTO();
            update.setAttributes(context.getAttributes() != null ? context.getAttributes() : "{\"segment\":\"default\"}");
            contextUpdateService.updateContext(customerId, update, "EnrichmentService");
        }
    }
}
