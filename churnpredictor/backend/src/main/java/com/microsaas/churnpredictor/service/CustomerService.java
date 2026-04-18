package com.microsaas.churnpredictor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.churnpredictor.dto.CustomerDto;
import com.microsaas.churnpredictor.entity.Customer;
import com.microsaas.churnpredictor.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final HealthScoreService healthScoreService;
    private final PredictionService predictionService;

    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        UUID tenantId = TenantContext.require();
        return customerRepository.findByTenantId(tenantId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(UUID id) {
        UUID tenantId = TenantContext.require();
        Customer customer = customerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToDto(customer);
    }

    @Transactional
    public CustomerDto createCustomer(CustomerDto dto) {
        UUID tenantId = TenantContext.require();
        Customer customer = new Customer();
        customer.setTenantId(tenantId);
        customer.setName(dto.getName());
        customer.setIndustry(dto.getIndustry());
        customer.setMrr(dto.getMrr());
        customer = customerRepository.save(customer);
        return mapToDto(customer);
    }

    private CustomerDto mapToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setIndustry(customer.getIndustry());
        dto.setMrr(customer.getMrr());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());

        try {
            dto.setLatestHealthScore(healthScoreService.getLatestHealthScore(customer.getId()));
        } catch(Exception e) {}
        
        try {
            dto.setLatestPrediction(predictionService.getLatestPrediction(customer.getId()));
        } catch(Exception e) {}

        return dto;
    }
}
