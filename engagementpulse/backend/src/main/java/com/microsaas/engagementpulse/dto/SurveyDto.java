package com.microsaas.engagementpulse.dto;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public class SurveyDto {
    public UUID id;
    public String title;
    public String description;
    public String status;
    public List<QuestionDto> questions;
    public LocalDateTime scheduledAt;
}
