package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.MarketDataDto;
import com.microsaas.compensationos.entity.MarketData;
import com.microsaas.compensationos.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final MarketDataRepository marketDataRepository;

    public List<MarketDataDto> getMarketData(String role, String location) {
        UUID tenantId = TenantContext.require();
        List<MarketData> data;
        if (role != null && location != null) {
            data = marketDataRepository.findByTenantIdAndRoleAndLocation(tenantId, role, location);
        } else if (role != null) {
            data = marketDataRepository.findByTenantIdAndRole(tenantId, role);
        } else {
            data = marketDataRepository.findByTenantId(tenantId);
        }
        return data.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public void importMarketData(List<MarketDataDto> dtos) {
        UUID tenantId = TenantContext.require();
        List<MarketData> entities = dtos.stream().map(dto -> {
            MarketData md = new MarketData();
            md.setTenantId(tenantId);
            md.setRole(dto.getRole());
            md.setLevel(dto.getLevel());
            md.setLocation(dto.getLocation());
            md.setP25Salary(dto.getP25Salary());
            md.setP50Salary(dto.getP50Salary());
            md.setP75Salary(dto.getP75Salary());
            md.setP90Salary(dto.getP90Salary());
            md.setDataSource(dto.getDataSource());
            md.setEffectiveDate(dto.getEffectiveDate());
            return md;
        }).collect(Collectors.toList());
        marketDataRepository.saveAll(entities);
    }

    private MarketDataDto mapToDto(MarketData entity) {
        MarketDataDto dto = new MarketDataDto();
        dto.setId(entity.getId());
        dto.setRole(entity.getRole());
        dto.setLevel(entity.getLevel());
        dto.setLocation(entity.getLocation());
        dto.setP25Salary(entity.getP25Salary());
        dto.setP50Salary(entity.getP50Salary());
        dto.setP75Salary(entity.getP75Salary());
        dto.setP90Salary(entity.getP90Salary());
        dto.setDataSource(entity.getDataSource());
        dto.setEffectiveDate(entity.getEffectiveDate());
        return dto;
    }
}
