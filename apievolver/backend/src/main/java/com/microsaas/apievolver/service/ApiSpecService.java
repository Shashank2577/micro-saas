package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.ApiSpec;
import com.microsaas.apievolver.model.ApiChange;
import com.microsaas.apievolver.repository.ApiSpecRepository;
import com.microsaas.apievolver.repository.ApiChangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiSpecService {
    private final ApiSpecRepository apiSpecRepository;
    private final ApiChangeRepository apiChangeRepository;
    private final SpecDiffService specDiffService;

    @Transactional
    public ApiSpec uploadSpec(String serviceId, String version, ApiSpec.SpecType type, String content) {
        Optional<ApiSpec> existingOpt = apiSpecRepository.findByServiceIdAndVersion(serviceId, version);
        if (existingOpt.isPresent()) {
            throw new IllegalArgumentException("Spec for this service and version already exists");
        }

        // Find the previous version (the latest uploaded one before this one)
        List<ApiSpec> existingSpecs = apiSpecRepository.findByServiceIdOrderByUploadedAtDesc(serviceId);
        ApiSpec previousSpec = existingSpecs.isEmpty() ? null : existingSpecs.get(0);

        ApiSpec newSpec = new ApiSpec();
        newSpec.setServiceId(serviceId);
        newSpec.setVersion(version);
        newSpec.setSpecType(type);
        newSpec.setSpecContent(content);
        newSpec.setUploadedAt(LocalDateTime.now());
        
        newSpec = apiSpecRepository.save(newSpec);

        if (previousSpec != null && previousSpec.getSpecType() == ApiSpec.SpecType.OPENAPI && type == ApiSpec.SpecType.OPENAPI) {
            ApiChange change = specDiffService.diff(previousSpec.getSpecContent(), content, previousSpec.getVersion(), version, newSpec.getId());
            apiChangeRepository.save(change);
        }

        return newSpec;
    }

    public List<ApiSpec> getSpecsForService(String serviceId) {
        return apiSpecRepository.findByServiceIdOrderByUploadedAtDesc(serviceId);
    }
    
    public List<ApiChange> getChangesForSpec(Long specId) {
        return apiChangeRepository.findBySpecId(specId);
    }
}
