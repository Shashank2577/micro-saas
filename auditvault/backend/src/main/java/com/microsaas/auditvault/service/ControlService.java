package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.model.Control;
import com.microsaas.auditvault.repository.ControlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ControlService {
    private final ControlRepository controlRepository;

    public List<Control> listControls(UUID frameworkId) {
        return controlRepository.findByTenantIdAndFrameworkId(TenantContext.require(), frameworkId);
    }
}
