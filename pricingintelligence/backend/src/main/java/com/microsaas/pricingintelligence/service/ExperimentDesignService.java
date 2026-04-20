package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.PricingExperiment;
import com.microsaas.pricingintelligence.repository.PricingExperimentRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExperimentDesignService {

    private final PricingExperimentRepository pricingExperimentRepository;

    public List<PricingExperiment> getExperiments() {
        return pricingExperimentRepository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public PricingExperiment createExperiment(PricingExperiment experiment) {
        experiment.setTenantId(TenantContext.require());
        return pricingExperimentRepository.save(experiment);
    }
}
