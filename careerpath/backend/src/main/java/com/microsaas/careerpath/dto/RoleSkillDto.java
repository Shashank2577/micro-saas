package com.microsaas.careerpath.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleSkillDto {
    private UUID skillId;
    private String skillName;
    private Integer requiredProficiency;
    private Boolean isCore;
}
