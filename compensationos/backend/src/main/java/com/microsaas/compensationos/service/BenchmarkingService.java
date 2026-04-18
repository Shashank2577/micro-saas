package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.BenchmarkingResponse;
import com.microsaas.compensationos.dto.EmployeeCompDto;
import com.microsaas.compensationos.dto.MarketDataDto;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BenchmarkingService {

    private final EmployeeCompensationRepository employeeCompensationRepository;
    private final MarketDataService marketDataService;
    private final EmployeeCompensationService employeeCompensationService;

    public BenchmarkingResponse getEmployeeBenchmark(UUID employeeRecordId) {
        UUID tenantId = TenantContext.require();
        EmployeeCompensation emp = employeeCompensationRepository.findById(employeeRecordId)
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeCompDto empDto = employeeCompensationService.mapToDto(emp);

        List<MarketDataDto> marketDataList = marketDataService.getMarketData(emp.getRole(), emp.getLocation());
        MarketDataDto md = marketDataList.stream()
                .filter(m -> m.getLevel().equalsIgnoreCase(emp.getLevel()))
                .findFirst()
                .orElse(null);

        BenchmarkingResponse response = new BenchmarkingResponse();
        response.setEmployee(empDto);
        response.setMarketData(md);

        if (md != null && md.getP50Salary() != null && md.getP50Salary().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal compaRatio = emp.getBaseSalary().divide(md.getP50Salary(), 2, RoundingMode.HALF_UP);
            response.setCompaRatio(compaRatio);

            if (md.getP90Salary() != null && md.getP25Salary() != null) {
                BigDecimal range = md.getP90Salary().subtract(md.getP25Salary());
                if (range.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal penetration = emp.getBaseSalary().subtract(md.getP25Salary())
                            .divide(range, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                    response.setRangePenetration(penetration + "%");
                }
            }
        }

        return response;
    }
}
