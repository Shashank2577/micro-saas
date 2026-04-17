package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.dto.EmployeeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    public List<Employee> getEmployees(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional
    public Employee createEmployee(UUID tenantId, EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setTenantId(tenantId);
        employee.setEmployeeNumber(dto.getEmployeeNumber());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setRole(dto.getRole());
        employee.setManagerId(dto.getManagerId());
        employee.setStatus(dto.getStatus());
        employee.setHireDate(dto.getHireDate());
        return repository.save(employee);
    }

    public Employee getEmployee(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }
}
