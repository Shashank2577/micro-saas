package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.BenchmarkingResponse;
import com.microsaas.compensationos.dto.EmployeeCompDto;
import com.microsaas.compensationos.dto.MarketDataDto;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BenchmarkingServiceTest {

    @Mock
    private EmployeeCompensationRepository employeeCompensationRepository;

    @Mock
    private MarketDataService marketDataService;

    @Mock
    private EmployeeCompensationService employeeCompensationService;

    @InjectMocks
    private BenchmarkingService benchmarkingService;

    @Test
    void getEmployeeBenchmark_CalculatesCompaRatio() {
        UUID tenantId = UUID.randomUUID();
        try (MockedStatic<TenantContext> mockedContext = mockStatic(TenantContext.class)) {
            mockedContext.when(TenantContext::require).thenReturn(tenantId);

            UUID empId = UUID.randomUUID();
            EmployeeCompensation emp = new EmployeeCompensation();
            emp.setId(empId);
            emp.setTenantId(tenantId);
            emp.setRole("Engineer");
            emp.setLocation("NY");
            emp.setLevel("L3");
            emp.setBaseSalary(new BigDecimal("120000"));

            EmployeeCompDto empDto = new EmployeeCompDto();
            empDto.setBaseSalary(new BigDecimal("120000"));

            MarketDataDto md = new MarketDataDto();
            md.setLevel("L3");
            md.setP25Salary(new BigDecimal("90000"));
            md.setP50Salary(new BigDecimal("100000"));
            md.setP75Salary(new BigDecimal("130000"));
            md.setP90Salary(new BigDecimal("150000"));

            when(employeeCompensationRepository.findById(empId)).thenReturn(Optional.of(emp));
            when(employeeCompensationService.mapToDto(emp)).thenReturn(empDto);
            when(marketDataService.getMarketData("Engineer", "NY")).thenReturn(List.of(md));

            BenchmarkingResponse response = benchmarkingService.getEmployeeBenchmark(empId);

            assertNotNull(response);
            assertEquals(new BigDecimal("1.20"), response.getCompaRatio());
            assertEquals("50.00%", response.getRangePenetration()); // (120 - 90) / (150 - 90) = 30 / 60 = 50%
        }
    }
}
