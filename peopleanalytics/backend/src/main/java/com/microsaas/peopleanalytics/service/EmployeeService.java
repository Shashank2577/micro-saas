package com.microsaas.peopleanalytics.service;

import com.crosscutting.starter.security.EncryptionService;
import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EncryptionService encryptionService;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllByTenantId(TenantContext.getTenantId());
    }

    @Transactional
    public Employee createEmployee(String firstName, String lastName, String email, String department, String role) {
        Employee employee = Employee.builder()
                .tenantId(TenantContext.getTenantId())
                .firstName(encryptionService.encrypt(firstName).getBytes())
                .lastName(encryptionService.encrypt(lastName).getBytes())
                .email(encryptionService.encrypt(email).getBytes())
                .department(department)
                .role(role)
                .status("ACTIVE")
                .build();
        return employeeRepository.save(employee);
    }

    public String getDecryptedEmail(Employee employee) {
        return encryptionService.decrypt(new String(employee.getEmail()));
    }
}
