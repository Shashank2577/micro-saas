package com.microsaas.careerpath.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.careerpath.client.LiteLLMClient;
import com.microsaas.careerpath.dto.*;
import com.microsaas.careerpath.entity.*;
import com.microsaas.careerpath.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CareerPathService {

    @Autowired private RoleRepository roleRepository;
    @Autowired private SkillRepository skillRepository;
    @Autowired private RoleSkillRepository roleSkillRepository;
    @Autowired private CareerPathRepository careerPathRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private EmployeeSkillRepository employeeSkillRepository;
    @Autowired private MentorshipRequestRepository mentorshipRequestRepository;
    @Autowired private DevelopmentPlanRepository developmentPlanRepository;
    @Autowired private LiteLLMClient liteLLMClient;

    public List<RoleDto> getAllRoles() {
        String tenantId = TenantContext.require().toString();
        return roleRepository.findAllByTenantId(tenantId).stream()
                .map(role -> RoleDto.builder()
                        .id(role.getId())
                        .title(role.getTitle())
                        .department(role.getDepartment())
                        .level(role.getLevel())
                        .description(role.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    public RoleDto getRoleById(UUID roleId) {
        String tenantId = TenantContext.require().toString();
        Role role = roleRepository.findByIdAndTenantId(roleId, tenantId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        List<RoleSkillDto> requiredSkills = roleSkillRepository.findAllByTenantIdAndRoleId(tenantId, roleId)
                .stream()
                .map(rs -> RoleSkillDto.builder()
                        .skillId(rs.getSkill().getId())
                        .skillName(rs.getSkill().getName())
                        .requiredProficiency(rs.getRequiredProficiency())
                        .isCore(rs.getIsCore())
                        .build())
                .collect(Collectors.toList());

        return RoleDto.builder()
                .id(role.getId())
                .title(role.getTitle())
                .department(role.getDepartment())
                .level(role.getLevel())
                .description(role.getDescription())
                .requiredSkills(requiredSkills)
                .build();
    }

    public RoadmapDto getRoadmap() {
        String tenantId = TenantContext.require().toString();
        List<Role> roles = roleRepository.findAllByTenantId(tenantId);
        List<CareerPath> paths = careerPathRepository.findAllByTenantId(tenantId);

        List<RoadmapNodeDto> nodes = new ArrayList<>();
        int x = 100, y = 100; // simple mock positioning
        for (Role role : roles) {
            Map<String, String> data = new HashMap<>();
            data.put("label", role.getTitle());
            Map<String, Integer> position = new HashMap<>();
            position.put("x", x);
            position.put("y", y);
            y += 100;
            
            nodes.add(RoadmapNodeDto.builder()
                    .id(role.getId().toString())
                    .type("default")
                    .data(data)
                    .position(position)
                    .build());
        }

        List<RoadmapEdgeDto> edges = paths.stream()
                .map(path -> RoadmapEdgeDto.builder()
                        .id("e" + path.getFromRole().getId() + "-" + path.getToRole().getId())
                        .source(path.getFromRole().getId().toString())
                        .target(path.getToRole().getId().toString())
                        .animated(true)
                        .build())
                .collect(Collectors.toList());

        return RoadmapDto.builder()
                .nodes(nodes)
                .edges(edges)
                .build();
    }

    public List<EmployeeSkillDto> getEmployeeSkills(UUID employeeId) {
        String tenantId = TenantContext.require().toString();
        return employeeSkillRepository.findAllByTenantIdAndEmployeeId(tenantId, employeeId)
                .stream()
                .map(es -> EmployeeSkillDto.builder()
                        .skillId(es.getSkill().getId())
                        .skillName(es.getSkill().getName())
                        .currentProficiency(es.getCurrentProficiency())
                        .build())
                .collect(Collectors.toList());
    }

    public void updateEmployeeSkill(UUID employeeId, SkillUpdateDto dto) {
        String tenantId = TenantContext.require().toString();
        Employee employee = employeeRepository.findByIdAndTenantId(employeeId, tenantId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Skill skill = skillRepository.findByIdAndTenantId(dto.getSkillId(), tenantId)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        Optional<EmployeeSkill> existingOpt = employeeSkillRepository.findByTenantIdAndEmployeeIdAndSkillId(tenantId, employeeId, skill.getId());
        if (existingOpt.isPresent()) {
            EmployeeSkill existing = existingOpt.get();
            existing.setCurrentProficiency(dto.getCurrentProficiency());
            employeeSkillRepository.save(existing);
        } else {
            EmployeeSkill es = EmployeeSkill.builder()
                    .tenantId(tenantId)
                    .employee(employee)
                    .skill(skill)
                    .currentProficiency(dto.getCurrentProficiency())
                    .build();
            employeeSkillRepository.save(es);
        }
    }

    public List<SkillGapDto> calculateSkillGaps(UUID employeeId, UUID targetRoleId) {
        String tenantId = TenantContext.require().toString();
        
        List<EmployeeSkill> employeeSkills = employeeSkillRepository.findAllByTenantIdAndEmployeeId(tenantId, employeeId);
        Map<UUID, Integer> employeeSkillMap = employeeSkills.stream()
                .collect(Collectors.toMap(es -> es.getSkill().getId(), EmployeeSkill::getCurrentProficiency));

        List<RoleSkill> targetRoleSkills = roleSkillRepository.findAllByTenantIdAndRoleId(tenantId, targetRoleId);

        List<SkillGapDto> gaps = new ArrayList<>();
        for (RoleSkill rs : targetRoleSkills) {
            UUID skillId = rs.getSkill().getId();
            int required = rs.getRequiredProficiency();
            int current = employeeSkillMap.getOrDefault(skillId, 0);
            
            if (current < required) {
                gaps.add(SkillGapDto.builder()
                        .skillId(skillId)
                        .skillName(rs.getSkill().getName())
                        .currentProficiency(current)
                        .requiredProficiency(required)
                        .gap(required - current)
                        .build());
            }
        }
        return gaps;
    }

    public String generateRoleRecommendations(UUID employeeId) {
        String tenantId = TenantContext.require().toString();
        Employee emp = employeeRepository.findByIdAndTenantId(employeeId, tenantId).orElseThrow();
        List<EmployeeSkillDto> skills = getEmployeeSkills(employeeId);
        
        String prompt = "Employee Goals: " + emp.getCareerGoals() + "\nSkills: " + 
                        skills.stream().map(s -> s.getSkillName() + " (" + s.getCurrentProficiency() + ")").collect(Collectors.joining(", "));
        
        return liteLLMClient.generateCompletion(
            "You are a career progression expert. Given an employee's skills and goals, recommend 3 target roles they should consider for their next career move. Format as a markdown list.",
            prompt
        );
    }

    public String generateLearningPaths(UUID employeeId) {
        String tenantId = TenantContext.require().toString();
        Employee emp = employeeRepository.findByIdAndTenantId(employeeId, tenantId).orElseThrow();
        List<EmployeeSkillDto> skills = getEmployeeSkills(employeeId);
        
        String prompt = "Suggest learning paths (courses, certifications, projects) for an employee with these skills: " + 
                        skills.stream().map(EmployeeSkillDto::getSkillName).collect(Collectors.joining(", "));
                        
        return liteLLMClient.generateCompletion(
            "You are a learning and development coach. Recommend a learning path for the employee.",
            prompt
        );
    }
    
    public String generateMentorMatch(String careerGoals) {
        return liteLLMClient.generateCompletion(
            "You are an AI matching mentees with mentors. Suggest the type of mentor an employee with the following goals should look for.",
            "Mentee Goals: " + careerGoals
        );
    }
    
    public String assessPromotionReadiness(UUID employeeId) {
        String tenantId = TenantContext.require().toString();
        Employee emp = employeeRepository.findByIdAndTenantId(employeeId, tenantId).orElseThrow();
        if (emp.getCurrentRole() == null) return "No current role defined.";
        
        return liteLLMClient.generateCompletion(
            "You are an HR expert. Evaluate promotion readiness based on the employee's current role and skills.",
            "Current Role: " + emp.getCurrentRole().getTitle() + "\nAssess their readiness for the next level."
        );
    }

    public String getCoachingGuidance(UUID managerId, UUID employeeId) {
        String tenantId = TenantContext.require().toString();
        Employee emp = employeeRepository.findByIdAndTenantId(employeeId, tenantId).orElseThrow();
        return liteLLMClient.generateCompletion(
            "You are an executive coach. Provide coaching guidance for a manager to discuss career development with their employee.",
            "Employee goals: " + emp.getCareerGoals()
        );
    }
}
