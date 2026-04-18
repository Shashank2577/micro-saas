package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.ChurnPredictionDto;
import com.microsaas.churnpredictor.entity.ChurnPrediction;
import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.repository.ChurnPredictionRepository;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredictionService {
    private final ChurnPredictionRepository predictionRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<ChurnPredictionDto> getLatestPredictions() {
        UUID tenantId = TenantContext.require();
        return predictionRepository.findLatestPredictionsByTenantId(tenantId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChurnPredictionDto> getPredictionHistory(UUID customerId) {
        UUID tenantId = TenantContext.require();
        return predictionRepository.findByTenantIdAndCustomerIdOrderByPredictedAtDesc(tenantId, customerId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChurnPredictionDto getLatestPrediction(UUID customerId) {
        UUID tenantId = TenantContext.require();
        return predictionRepository.findFirstByTenantIdAndCustomerIdOrderByPredictedAtDesc(tenantId, customerId)
                .map(this::mapToDto).orElse(null);
    }

    @Transactional
    public void recalculatePredictions() {
        UUID tenantId = TenantContext.require();
        List<Customer> customers = customerRepository.findByTenantId(tenantId);
        
        for (Customer customer : customers) {
            ChurnPrediction prediction = new ChurnPrediction();
            prediction.setTenantId(tenantId);
            prediction.setCustomer(customer);
            
            // Mock prediction values
            double prob30 = Math.random();
            prediction.setProbability30Days(BigDecimal.valueOf(prob30));
            prediction.setProbability60Days(BigDecimal.valueOf(Math.min(1.0, prob30 + 0.1)));
            prediction.setProbability90Days(BigDecimal.valueOf(Math.min(1.0, prob30 + 0.2)));
            
            if (prob30 > 0.7) {
                prediction.setRiskSegment("HIGH");
            } else if (prob30 > 0.3) {
                prediction.setRiskSegment("MEDIUM");
            } else {
                prediction.setRiskSegment("LOW");
            }
            
            predictionRepository.save(prediction);
        }
    }

    private ChurnPredictionDto mapToDto(ChurnPrediction entity) {
        ChurnPredictionDto dto = new ChurnPredictionDto();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setRiskSegment(entity.getRiskSegment());
        dto.setProbability30Days(entity.getProbability30Days());
        dto.setProbability60Days(entity.getProbability60Days());
        dto.setProbability90Days(entity.getProbability90Days());
        dto.setPredictedAt(entity.getPredictedAt());
        return dto;
    }
}
