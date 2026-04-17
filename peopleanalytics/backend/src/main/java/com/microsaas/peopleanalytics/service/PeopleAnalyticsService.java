package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.*;
import com.microsaas.peopleanalytics.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PeopleAnalyticsService {

    private final HeadcountPlanRepository headcountPlanRepository;
    private final HeadcountLineRepository headcountLineRepository;
    private final OrgHealthMetricRepository orgHealthMetricRepository;
    private final SkillsGapRepository skillsGapRepository;
    private final WorkforceScenarioRepository workforceScenarioRepository;

    @Transactional
    public HeadcountPlan createHeadcountPlan(UUID tenantId, HeadcountPlan plan) {
        plan.setId(UUID.randomUUID());
        plan.setTenantId(tenantId);
        plan.setCreatedAt(LocalDateTime.now());
        if (plan.getTotalCost() == null) {
            plan.setTotalCost(BigDecimal.ZERO);
        }
        return headcountPlanRepository.save(plan);
    }

    public List<HeadcountPlan> getHeadcountPlans(UUID tenantId) {
        return headcountPlanRepository.findByTenantId(tenantId);
    }

    public HeadcountPlan getHeadcountPlan(UUID tenantId, UUID id) {
        return headcountPlanRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public Map<String, Object> compareScenarios(UUID tenantId, UUID scenario1Id, UUID scenario2Id) {
        WorkforceScenario s1 = workforceScenarioRepository.findByIdAndTenantId(scenario1Id, tenantId)
                .orElseThrow(() -> new RuntimeException("Scenario 1 not found"));
        WorkforceScenario s2 = workforceScenarioRepository.findByIdAndTenantId(scenario2Id, tenantId)
                .orElseThrow(() -> new RuntimeException("Scenario 2 not found"));

        Map<String, Object> comparison = new HashMap<>();
        comparison.put("scenario1", s1);
        comparison.put("scenario2", s2);
        comparison.put("headcountDifference", s2.getProjectedHeadcount() - s1.getProjectedHeadcount());
        comparison.put("burnRateDifference", s2.getBurnRateMonthly().subtract(s1.getBurnRateMonthly()));
        return comparison;
    }

    public List<OrgHealthMetric> getOrgHealthMetrics(UUID tenantId) {
        return orgHealthMetricRepository.findByTenantId(tenantId);
    }

    public List<SkillsGap> getSkillsGaps(UUID tenantId, String department) {
        if (department != null && !department.isEmpty()) {
            return skillsGapRepository.findByDepartmentAndTenantId(department, tenantId);
        }
        return skillsGapRepository.findByTenantId(tenantId);
    }

    @Transactional
    public WorkforceScenario modelScenario(UUID tenantId, WorkforceScenario scenarioRequest) {
        // Mock AI modeling logic - simple proportional projection based on assumptions
        scenarioRequest.setId(UUID.randomUUID());
        scenarioRequest.setTenantId(tenantId);

        // Simple projection if not provided
        if (scenarioRequest.getProjectedHeadcount() == 0) {
            scenarioRequest.setProjectedHeadcount((int) (scenarioRequest.getBaselineHeadcount() * 1.2));
        }
        if (scenarioRequest.getBurnRateMonthly() == null) {
            scenarioRequest.setBurnRateMonthly(new BigDecimal(scenarioRequest.getProjectedHeadcount() * 10000));
        }
        if (scenarioRequest.getRunwayMonths() == null) {
            scenarioRequest.setRunwayMonths(new BigDecimal("12.0"));
        }

        return workforceScenarioRepository.save(scenarioRequest);
    }
}
