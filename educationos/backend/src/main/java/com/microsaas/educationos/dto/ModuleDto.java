package com.microsaas.educationos.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ModuleDto {
    private UUID id;
    private UUID courseId;
    private String title;
    private String content;
    private String difficultyLevel;
    private Integer orderIndex;
}
