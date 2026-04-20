package com.microsaas.pricingintelligence.service;

import com.microsaas.pricingintelligence.domain.ElasticityModel;
import com.microsaas.pricingintelligence.repository.ElasticityModelRepository;
import com.crosscutting.starter.queue.QueueService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ElasticityModelingServiceTest {

    @Mock
    private ElasticityModelRepository elasticityModelRepository;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private ElasticityModelingService elasticityModelingService;

    private final UUID tenantId = UUID.randomUUID();
    private final UUID segmentId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        TenantContext.set(tenantId);
    }

    @Test
    public void calculateElasticity_createsAndPublishesModel() {
        when(elasticityModelRepository.findByTenantIdAndSegmentId(tenantId, segmentId))
                .thenReturn(Optional.empty());

        ElasticityModel savedModel = new ElasticityModel();
        savedModel.setId(UUID.randomUUID());
        savedModel.setTenantId(tenantId);
        savedModel.setSegmentId(segmentId);

        when(elasticityModelRepository.save(any(ElasticityModel.class))).thenReturn(savedModel);

        ElasticityModel result = elasticityModelingService.calculateElasticity(segmentId);

        assertNotNull(result);
        verify(elasticityModelRepository).save(any(ElasticityModel.class));
        verify(queueService).enqueue(eq("model-training-queue"), anyString(), eq(0));
    }
}
