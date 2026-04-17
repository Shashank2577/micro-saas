package com.microsaas.compbenchmark.services;

import com.microsaas.compbenchmark.model.CompGap;
import com.microsaas.compbenchmark.model.EmployeeComp;
import com.microsaas.compbenchmark.model.MarketBenchmark;
import com.microsaas.compbenchmark.model.PayEquityAudit;
import com.microsaas.compbenchmark.repositories.CompGapRepository;
import com.microsaas.compbenchmark.repositories.EmployeeCompRepository;
import com.microsaas.compbenchmark.repositories.MarketBenchmarkRepository;
import com.microsaas.compbenchmark.repositories.PayEquityAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompBenchmarkService {

    private final EmployeeCompRepository employeeCompRepository;
    private final MarketBenchmarkRepository marketBenchmarkRepository;
    private final CompGapRepository compGapRepository;
    private final PayEquityAuditRepository payEquityAuditRepository;

    @Transactional
    public EmployeeComp addEmployeeComp(EmployeeComp employeeComp) {
        return employeeCompRepository.save(employeeComp);
    }

    public List<EmployeeComp> getAllEmployeeComps() {
        return employeeCompRepository.findAll();
    }

    public Optional<MarketBenchmark> getMarketBenchmark(String title, String level) {
        return marketBenchmarkRepository.findByTitleAndLevel(title, level);
    }

    @Transactional
    public List<CompGap> computeGaps() {
        List<EmployeeComp> employees = employeeCompRepository.findAll();
        compGapRepository.deleteAll(); // clear previous gaps for the current tenant context

        List<CompGap> gaps = employees.stream().map(emp -> {
            Optional<MarketBenchmark> benchmarkOpt = marketBenchmarkRepository.findByTitleAndLevel(emp.getTitle(), emp.getLevel());
            if (benchmarkOpt.isEmpty()) {
                return null; // Skip if no benchmark
            }

            MarketBenchmark benchmark = benchmarkOpt.get();
            BigDecimal gapAmount = emp.getTotalComp().subtract(benchmark.getP50());
            BigDecimal gapPct = gapAmount.divide(benchmark.getP50(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

            CompGap.RiskLevel riskLevel;
            if (gapPct.compareTo(new BigDecimal("-10")) <= 0) {
                riskLevel = CompGap.RiskLevel.CRITICAL;
            } else if (gapPct.compareTo(new BigDecimal("-5")) <= 0) {
                riskLevel = CompGap.RiskLevel.HIGH;
            } else if (gapPct.compareTo(new BigDecimal("0")) < 0) {
                riskLevel = CompGap.RiskLevel.MEDIUM;
            } else {
                riskLevel = CompGap.RiskLevel.LOW;
            }

            return CompGap.builder()
                    .employeeId(emp.getEmployeeId())
                    .gapAmount(gapAmount)
                    .gapPct(gapPct)
                    .riskLevel(riskLevel)
                    .recommendedRangeMin(benchmark.getP25())
                    .recommendedRangeMax(benchmark.getP75())
                    .build();
        }).filter(gap -> gap != null).map(gap -> (CompGap) gap).toList();

        return compGapRepository.saveAll(gaps);
    }

    public List<PayEquityAudit> getPayEquityAudit(PayEquityAudit.GroupDimension dimension) {
        if (dimension != null) {
            return payEquityAuditRepository.findByGroupDimension(dimension);
        }
        return payEquityAuditRepository.findAll();
    }

    @Transactional
    public List<PayEquityAudit> computePayEquityAudit() {
        // Simplified mock logic since dimension data is not strictly in employee_comp
        // Real implementation would calculate properly based on dimension
        payEquityAuditRepository.deleteAll();
        PayEquityAudit audit = PayEquityAudit.builder()
            .groupDimension(PayEquityAudit.GroupDimension.GENDER)
            .avgGapPct(new BigDecimal("-4.5"))
            .flaggedCount(12)
            .auditDate(LocalDate.now())
            .build();
        return List.of(payEquityAuditRepository.save(audit));
    }

    public Map<String, BigDecimal> getOfferRecommendation(String employeeId) {
        EmployeeComp emp = employeeCompRepository.findByEmployeeId(employeeId).orElseThrow();
        MarketBenchmark benchmark = marketBenchmarkRepository.findByTitleAndLevel(emp.getTitle(), emp.getLevel()).orElseThrow();

        return Map.of(
            "recommendedMin", benchmark.getP25(),
            "recommendedMax", benchmark.getP75(),
            "targetP50", benchmark.getP50()
        );
    }
}
