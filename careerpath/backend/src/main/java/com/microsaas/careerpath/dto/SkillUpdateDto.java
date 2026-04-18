package com.microsaas.careerpath.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillUpdateDto {
    private UUID skillId;
    private Integer currentProficiency;
}
