package com.microsaas.peopleanalytics.service;

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

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllByTenantId(UUID.randomUUID());
    }

    @Transactional
    public Employee createEmployee(String firstName, String lastName, String email, String department, String role) {
        Employee employee = Employee.builder()
                .tenantId(UUID.randomUUID())
                .firstName(firstName.getBytes())
                .lastName(lastName.getBytes())
                .email(email.getBytes())
                .department(department)
                .role(role)
                .status("ACTIVE")
                .build();
        return employeeRepository.save(employee);
    }

    public String getDecryptedEmail(Employee employee) {
        return new String(employee.getEmail());
    }
}
