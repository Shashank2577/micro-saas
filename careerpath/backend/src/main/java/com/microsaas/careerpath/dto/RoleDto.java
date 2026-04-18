package com.microsaas.careerpath.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private UUID id;
    private String title;
    private String department;
    private Integer level;
    private String description;
    private List<RoleSkillDto> requiredSkills;
}
