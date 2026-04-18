package com.microsaas.educationos.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class LearnerProfileDto {
    private UUID id;
    private UUID userId;
    private String backgroundInfo;
    private String learningStyle;
}
