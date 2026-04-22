package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.model.Framework;
import com.microsaas.auditvault.repository.FrameworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FrameworkService {
    private final FrameworkRepository frameworkRepository;

    public List<Framework> listFrameworks() {
        return frameworkRepository.findByTenantId(TenantContext.require());
    }
}
