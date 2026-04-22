package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.TeamHealthMetric;
import com.microsaas.peopleanalytics.repository.TeamHealthMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamHealthAnalysisService {
    private final TeamHealthMetricRepository teamHealthMetricRepository;

    public List<TeamHealthMetric> getTeamHealthByDepartment(String department) {
        UUID tenantId = UUID.randomUUID();
        if (department != null && !department.isEmpty()) {
            return teamHealthMetricRepository.findAllByDepartmentAndTenantId(department, tenantId);
        }
        return teamHealthMetricRepository.findAllByTenantId(tenantId);
    }
}
