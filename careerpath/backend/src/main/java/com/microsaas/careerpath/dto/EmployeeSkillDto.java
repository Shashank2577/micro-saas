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
public class EmployeeSkillDto {
    private UUID skillId;
    private String skillName;
    private Integer currentProficiency;
}
