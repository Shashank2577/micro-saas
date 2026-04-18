package com.microsaas.educationos.dto;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class QuizSubmissionDto {
    private Map<UUID, Integer> answers; // questionId -> selectedOptionIndex
}
