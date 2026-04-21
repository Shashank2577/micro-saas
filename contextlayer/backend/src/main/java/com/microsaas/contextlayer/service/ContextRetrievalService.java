package com.microsaas.contextlayer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.contextlayer.domain.CustomerContext;
import com.microsaas.contextlayer.repository.CustomerContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContextRetrievalService {
    private final CustomerContextRepository customerContextRepository;

    public Optional<CustomerContext> getContext(String customerId) {
        return customerContextRepository.findByCustomerIdAndTenantId(customerId, TenantContext.require());
    }
}
