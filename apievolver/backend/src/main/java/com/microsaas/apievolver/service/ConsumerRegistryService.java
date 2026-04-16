package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.ApiDependency;
import com.microsaas.apievolver.repository.ApiDependencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumerRegistryService {
    private final ApiDependencyRepository repository;

    public ApiDependency registerDependency(String consumer, String provider, List<String> endpoints, boolean sensitive) {
        ApiDependency dep = new ApiDependency();
        dep.setConsumerServiceId(consumer);
        dep.setProviderServiceId(provider);
        dep.setUsedEndpoints(endpoints);
        dep.setSensitiveToBreakingChanges(sensitive);
        return repository.save(dep);
    }

    public List<ApiDependency> getConsumersOf(String providerId) {
        return repository.findByProviderServiceId(providerId);
    }

    public List<ApiDependency> getDependenciesOf(String consumerId) {
        return repository.findByConsumerServiceId(consumerId);
    }
}
