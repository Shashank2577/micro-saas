package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.CustomerHealthScoreDto;
import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.entity.CustomerHealthScore;
import com.microsaas.churnpredictor.repository.CustomerHealthScoreRepository;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthScoreService {
    private final CustomerHealthScoreRepository healthScoreRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<CustomerHealthScoreDto> getHealthScoreHistory(UUID customerId) {
        UUID tenantId = TenantContext.require();
        return healthScoreRepository.findByTenantIdAndCustomerIdOrderByCalculatedAtDesc(tenantId, customerId)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerHealthScoreDto getLatestHealthScore(UUID customerId) {
        UUID tenantId = TenantContext.require();
        return healthScoreRepository.findFirstByTenantIdAndCustomerIdOrderByCalculatedAtDesc(tenantId, customerId)
                .map(this::mapToDto).orElse(null);
    }

    @Transactional
    public CustomerHealthScoreDto recalculateHealthScore(UUID customerId) {
        UUID tenantId = TenantContext.require();
        Customer customer = customerRepository.findByIdAndTenantId(customerId, tenantId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Mock calculation
        CustomerHealthScore score = new CustomerHealthScore();
        score.setTenantId(tenantId);
        score.setCustomer(customer);
        score.setUsageScore(BigDecimal.valueOf(Math.random() * 100));
        score.setSupportScore(BigDecimal.valueOf(Math.random() * 100));
        score.setEngagementScore(BigDecimal.valueOf(Math.random() * 100));
        score.setNpsScore(BigDecimal.valueOf(Math.random() * 100));

        BigDecimal composite = score.getUsageScore().add(score.getSupportScore())
                .add(score.getEngagementScore()).add(score.getNpsScore())
                .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
        score.setCompositeScore(composite);

        score = healthScoreRepository.save(score);
        return mapToDto(score);
    }

    private CustomerHealthScoreDto mapToDto(CustomerHealthScore entity) {
        CustomerHealthScoreDto dto = new CustomerHealthScoreDto();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setCompositeScore(entity.getCompositeScore());
        dto.setUsageScore(entity.getUsageScore());
        dto.setSupportScore(entity.getSupportScore());
        dto.setEngagementScore(entity.getEngagementScore());
        dto.setNpsScore(entity.getNpsScore());
        dto.setCalculatedAt(entity.getCalculatedAt());
        return dto;
    }
}
