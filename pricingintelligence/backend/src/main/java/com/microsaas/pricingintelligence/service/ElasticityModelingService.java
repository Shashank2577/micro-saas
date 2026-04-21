package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.repository.ElasticityModelRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import com.crosscutting.starter.queue.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ElasticityModelingService {

    private final ElasticityModelRepository elasticityModelRepository;
    private final QueueService queueService;

    @Transactional
    public ElasticityModel calculateElasticity(UUID segmentId) {
        UUID tenantId = TenantContext.require();

        Optional<ElasticityModel> existing = elasticityModelRepository.findByTenantIdAndSegmentId(tenantId, segmentId);

        ElasticityModel model = existing.orElse(new ElasticityModel());
        model.setTenantId(tenantId);
        model.setSegmentId(segmentId);

        // Simulating Sklearn Model Calculation
        model.setPriceRangeMin(new BigDecimal("10.00"));
        model.setPriceRangeMax(new BigDecimal("100.00"));
        model.setElasticityCoefficient(-1.5);
        model.setRSquared(0.85);

        model = elasticityModelRepository.save(model);

        // Publish event to queue
        queueService.enqueue("model-training-queue", "{\"type\":\"pricing-model.trained\",\"segmentId\":\"" + segmentId + "\"}", 0);

        return model;
    }
}
