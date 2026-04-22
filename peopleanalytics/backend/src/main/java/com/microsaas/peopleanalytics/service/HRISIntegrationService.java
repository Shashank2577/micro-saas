package com.microsaas.peopleanalytics.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HRISIntegrationService {
    private final EmployeeService employeeService;

    public void bulkSync(List<Map<String, String>> data) {
        UUID tenantId = UUID.randomUUID();
        log.info("Starting bulk HRIS sync for tenant {}", tenantId);

        for (Map<String, String> record : data) {
            try {
                employeeService.createEmployee(
                    record.getOrDefault("firstName", ""),
                    record.getOrDefault("lastName", ""),
                    record.getOrDefault("email", ""),
                    record.getOrDefault("department", ""),
                    record.getOrDefault("role", "")
                );
            } catch (Exception e) {
                log.error("Failed to sync record", e);
            }
        }
    }
}
