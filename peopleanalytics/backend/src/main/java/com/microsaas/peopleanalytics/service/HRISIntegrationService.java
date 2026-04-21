package com.microsaas.peopleanalytics.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HRISIntegrationService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    @Transactional
    public void bulkSync(List<Map<String, String>> employeeData) {
        UUID tenantId = TenantContext.getTenantId();
        log.info("Starting bulk sync for tenant: {}", tenantId);

        for (Map<String, String> data : employeeData) {
            String externalId = data.get("externalId");
            employeeRepository.findByExternalIdAndTenantId(externalId, tenantId)
                .ifPresentOrElse(
                    existing -> updateEmployee(existing, data),
                    () -> createNewFromSync(data, tenantId)
                );
        }
    }

    private void updateEmployee(Employee employee, Map<String, String> data) {
        employee.setDepartment(data.get("department"));
        employee.setRole(data.get("role"));
        employee.setStatus(data.get("status"));
        employeeRepository.save(employee);
    }

    private void createNewFromSync(Map<String, String> data, UUID tenantId) {
        Employee employee = employeeService.createEmployee(
            data.get("firstName"),
            data.get("lastName"),
            data.get("email"),
            data.get("department"),
            data.get("role")
        );
        employee.setExternalId(data.get("externalId"));
        employeeRepository.save(employee);
    }
}
