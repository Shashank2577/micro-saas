package com.microsaas.compensationos.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.compensationos.dto.EmployeeCompDto;
import com.microsaas.compensationos.entity.EmployeeCompensation;
import com.microsaas.compensationos.repository.EmployeeCompensationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeCompensationService {

    private final EmployeeCompensationRepository employeeCompensationRepository;

    public List<EmployeeCompDto> getEmployees(String department, String role) {
        UUID tenantId = TenantContext.require();
        List<EmployeeCompensation> data;
        if (department != null) {
            data = employeeCompensationRepository.findByTenantIdAndDepartment(tenantId, department);
        } else if (role != null) {
            data = employeeCompensationRepository.findByTenantIdAndRole(tenantId, role);
        } else {
            data = employeeCompensationRepository.findByTenantId(tenantId);
        }
        return data.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public EmployeeCompDto saveEmployee(EmployeeCompDto dto) {
        UUID tenantId = TenantContext.require();
        EmployeeCompensation entity = new EmployeeCompensation();
        if (dto.getId() != null) {
            entity = employeeCompensationRepository.findById(dto.getId())
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElse(entity);
        }
        entity.setTenantId(tenantId);
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setRole(dto.getRole());
        entity.setLevel(dto.getLevel());
        entity.setDepartment(dto.getDepartment());
        entity.setLocation(dto.getLocation());
        entity.setBaseSalary(dto.getBaseSalary());
        entity.setBonusTargetPercent(dto.getBonusTargetPercent());
        entity.setEquityGrantValue(dto.getEquityGrantValue());
        entity.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "USD");
        entity.setGender(dto.getGender());
        entity.setEthnicity(dto.getEthnicity());
        entity.setHireDate(dto.getHireDate());

        return mapToDto(employeeCompensationRepository.save(entity));
    }

    public EmployeeCompDto mapToDto(EmployeeCompensation entity) {
        EmployeeCompDto dto = new EmployeeCompDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRole(entity.getRole());
        dto.setLevel(entity.getLevel());
        dto.setDepartment(entity.getDepartment());
        dto.setLocation(entity.getLocation());
        dto.setBaseSalary(entity.getBaseSalary());
        dto.setBonusTargetPercent(entity.getBonusTargetPercent());
        dto.setEquityGrantValue(entity.getEquityGrantValue());
        dto.setCurrency(entity.getCurrency());
        dto.setGender(entity.getGender());
        dto.setEthnicity(entity.getEthnicity());
        dto.setHireDate(entity.getHireDate());
        return dto;
    }
}
