package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.model.Employee;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledPredictionService {

    private final RetentionPredictionService predictionService;
    private final EmployeeRepository employeeRepository;

    @Scheduled(cron = "0 0 2 * * ?") // Daily at 2 AM
    public void refreshPredictions() {
        // In a multi-tenant environment, we'd iterate over active tenants
        // For simplicity, find all distinct tenants from employees
        List<Employee> allEmps = employeeRepository.findAll();
        allEmps.stream().map(Employee::getTenantId).distinct().forEach(predictionService::predictRisks);
    }
}
