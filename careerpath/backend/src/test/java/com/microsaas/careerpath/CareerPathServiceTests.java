package com.microsaas.careerpath;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.careerpath.dto.*;
import com.microsaas.careerpath.entity.*;
import com.microsaas.careerpath.repository.*;
import com.microsaas.careerpath.service.CareerPathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CareerPathServiceTests {

    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private RoleSkillRepository roleSkillRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeSkillRepository employeeSkillRepository;

    @InjectMocks
    private CareerPathService careerPathService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TenantContext.set(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }

    @Test
    void getAllRoles_shouldReturnRoles() {
        Role role = Role.builder().id(UUID.randomUUID()).tenantId("00000000-0000-0000-0000-000000000001").title("Dev").build();
        when(roleRepository.findAllByTenantId("00000000-0000-0000-0000-000000000001")).thenReturn(List.of(role));

        List<RoleDto> result = careerPathService.getAllRoles();

        assertEquals(1, result.size());
        assertEquals("Dev", result.get(0).getTitle());
    }

    @Test
    void getRoleById_shouldReturnRole() {
        UUID roleId = UUID.randomUUID();
        Role role = Role.builder().id(roleId).tenantId("00000000-0000-0000-0000-000000000001").title("Dev").build();
        when(roleRepository.findByIdAndTenantId(roleId, "00000000-0000-0000-0000-000000000001")).thenReturn(Optional.of(role));
        when(roleSkillRepository.findAllByTenantIdAndRoleId("00000000-0000-0000-0000-000000000001", roleId)).thenReturn(List.of());

        RoleDto result = careerPathService.getRoleById(roleId);

        assertEquals(roleId, result.getId());
        assertEquals("Dev", result.getTitle());
    }

    @Test
    void calculateSkillGaps_shouldReturnGaps() {
        UUID employeeId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        Skill skill1 = Skill.builder().id(UUID.randomUUID()).name("Java").build();

        EmployeeSkill empSkill = EmployeeSkill.builder().skill(skill1).currentProficiency(2).build();
        when(employeeSkillRepository.findAllByTenantIdAndEmployeeId("00000000-0000-0000-0000-000000000001", employeeId))
                .thenReturn(List.of(empSkill));

        RoleSkill roleSkill = RoleSkill.builder().skill(skill1).requiredProficiency(4).build();
        when(roleSkillRepository.findAllByTenantIdAndRoleId("00000000-0000-0000-0000-000000000001", roleId))
                .thenReturn(List.of(roleSkill));

        List<SkillGapDto> gaps = careerPathService.calculateSkillGaps(employeeId, roleId);

        assertEquals(1, gaps.size());
        assertEquals("Java", gaps.get(0).getSkillName());
        assertEquals(2, gaps.get(0).getCurrentProficiency());
        assertEquals(4, gaps.get(0).getRequiredProficiency());
        assertEquals(2, gaps.get(0).getGap());
    }
}
