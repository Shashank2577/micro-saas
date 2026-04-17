package com.microsaas.peopleanalytics.dto;

import lombok.Data;
import java.util.UUID;
import java.util.Map;

@Data
public class SurveyResponseDto {
    private UUID employeeId;
    private Map<String, Object> responses;
}
