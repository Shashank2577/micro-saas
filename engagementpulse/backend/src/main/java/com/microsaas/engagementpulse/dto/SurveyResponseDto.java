package com.microsaas.engagementpulse.dto;

import java.util.List;
import java.util.UUID;

public class SurveyResponseDto {
    public UUID surveyId;
    public UUID employeeId;
    public UUID teamId;
    public List<AnswerDto> answers;
}
